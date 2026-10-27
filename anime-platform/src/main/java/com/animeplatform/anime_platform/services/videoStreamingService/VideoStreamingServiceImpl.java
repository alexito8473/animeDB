package com.animeplatform.anime_platform.services.videoStreamingService;

import com.animeplatform.anime_platform.config.exceptions.ResourceNotFoundException;
import com.animeplatform.anime_platform.models.EpisodeModel;
import com.animeplatform.anime_platform.models.MediaFileModel;
import com.animeplatform.anime_platform.models.MediaModel;
import com.animeplatform.anime_platform.repositories.EpisodeRepository;
import com.animeplatform.anime_platform.repositories.MediaFileRepository;
import com.animeplatform.anime_platform.repositories.MediaRepository;
import com.animeplatform.anime_platform.utils.CheckAndThrows;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VideoStreamingServiceImpl implements VideoStreamingService {

    private static final Pattern RANGE_PATTERN =
            Pattern.compile("^bytes=(\\d*)-(\\d*)$");

    @Autowired
    private MediaFileRepository files;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Value("${app.media-storage-root}")
    private String storageRoot;

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public ResponseEntity<InputStreamResource> streamBySlug(String slug, String rangeHeader) {

        Optional<MediaModel> mediaOptional = mediaRepository.findBySlug(slug);

        if (mediaOptional.isPresent()) {
            MediaModel media = mediaOptional.get();
            MediaFileModel file = CheckAndThrows.checkOptionalIfNullResourceNotFoundException(
                    files.findFirstByMedia_IdAndFileTypeOrderByIdDesc(media.getId(), "ORIGINAL"),
                    "No hay vídeo disponible para: " + slug);

            return streamFile(file, rangeHeader);
        }

        EpisodeModel episode = CheckAndThrows.checkOptionalIfNullResourceNotFoundException(
                episodeRepository.findBySlug(slug),
                "No se encontró contenido con slug: " + slug);

        MediaFileModel file = CheckAndThrows.checkOptionalIfNullResourceNotFoundException(
                files.findFirstByEpisode_IdAndFileTypeOrderByIdDesc(episode.getId(), "ORIGINAL"),
                "No hay vídeo disponible para el episodio: " + slug);

        return streamFile(file, rangeHeader);
    }

    private ResponseEntity<InputStreamResource> streamFile(
            MediaFileModel file, String rangeHeader) {

        try {
            Path root = Paths.get(storageRoot)
                    .toAbsolutePath()
                    .normalize();

            Path storedPath = Paths.get(file.getStoragePath());

            // Acepta tanto rutas absolutas como rutas relativas a storageRoot.
            Path videoPath = (storedPath.isAbsolute()
                    ? storedPath
                    : root.resolve(storedPath))
                    .toAbsolutePath()
                    .normalize();

            // Las rutas relativas deben permanecer dentro de storageRoot.
            // Para las absolutas se permite el directorio configurado y sus hijos.
            if (!videoPath.startsWith(root)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "La ruta del vídeo está fuera del directorio permitido");
            }


            if (!Files.exists(videoPath)) {
                throw new ResourceNotFoundException(
                        "El archivo no existe en esta ruta: " + videoPath
                );
            }

            if (!Files.isRegularFile(videoPath)) {
                throw new ResourceNotFoundException(
                        "La ruta no es un archivo normal: " + videoPath
                );
            }

            if (!Files.isReadable(videoPath)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Java no tiene permiso para leer el archivo: " + videoPath
                );
            }

            long fileSize = Files.size(videoPath);

            String mimeType = file.getMimeType() != null
                    ? file.getMimeType()
                    : "video/mp4";

            MediaType contentType;
            try {
                contentType = MediaType.parseMediaType(mimeType);
            } catch (IllegalArgumentException ex) {
                contentType = MediaType.APPLICATION_OCTET_STREAM;
            }

            if (rangeHeader == null || rangeHeader.isBlank()) {
                InputStream input = Files.newInputStream(videoPath);

                return ResponseEntity.ok()
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .contentType(contentType)
                        .contentLength(fileSize)
                        .body(new InputStreamResource(input));
            }

            Matcher matcher = RANGE_PATTERN.matcher(rangeHeader.trim());

            if (!matcher.matches() || fileSize == 0) {
                return rangeNotSatisfiable(fileSize);
            }

            String startText = matcher.group(1);
            String endText = matcher.group(2);

            if (startText.isEmpty() && endText.isEmpty()) {
                return rangeNotSatisfiable(fileSize);
            }

            long start;
            long end;

            try {
                if (startText.isEmpty()) {
                    // bytes=-500: los últimos 500 bytes.
                    long suffixLength = Long.parseLong(endText);

                    if (suffixLength <= 0) {
                        return rangeNotSatisfiable(fileSize);
                    }

                    start = Math.max(0, fileSize - suffixLength);
                    end = fileSize - 1;
                } else {
                    start = Long.parseLong(startText);
                    end = endText.isEmpty()
                            ? fileSize - 1
                            : Long.parseLong(endText);
                }
            } catch (NumberFormatException ex) {
                return rangeNotSatisfiable(fileSize);
            }

            if (start < 0 || start >= fileSize || end < start) {
                return rangeNotSatisfiable(fileSize);
            }

            end = Math.min(end, fileSize - 1);
            long contentLength = end - start + 1;

            InputStream input = Files.newInputStream(videoPath);

            try {
                input.skipNBytes(start);
            } catch (IOException ex) {
                input.close();
                throw ex;
            }

            InputStream limitedInput = new InputStream() {
                private long remaining = contentLength;

                @Override
                public int read() throws IOException {
                    if (remaining <= 0) {
                        return -1;
                    }

                    int value = input.read();
                    if (value != -1) {
                        remaining--;
                    }
                    return value;
                }

                @Override
                public int read(byte[] buffer, int offset, int length)
                        throws IOException {
                    if (remaining <= 0) {
                        return -1;
                    }

                    int bytesRead = input.read(
                            buffer, offset,
                            (int) Math.min(length, remaining));

                    if (bytesRead > 0) {
                        remaining -= bytesRead;
                    }

                    return bytesRead;
                }

                @Override
                public void close() throws IOException {
                    input.close();
                }
            };

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(
                            HttpHeaders.CONTENT_RANGE,
                            "bytes " + start + "-" + end + "/" + fileSize)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .body(new InputStreamResource(limitedInput));

        } catch (IOException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al transmitir el vídeo", ex);
        }
    }

    private ResponseEntity<InputStreamResource> rangeNotSatisfiable(
            long fileSize) {

        return ResponseEntity
                .status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                .build();
    }
}