package com.animeplatform.anime_platform.services.mediaService;

import com.animeplatform.anime_platform.config.exceptions.ResourceNotFoundException;
import com.animeplatform.anime_platform.dtos.MediaDetailDto;
import com.animeplatform.anime_platform.dtos.MediaRequestDto;
import com.animeplatform.anime_platform.dtos.MediaSummaryDto;
import com.animeplatform.anime_platform.models.MediaModel;
import com.animeplatform.anime_platform.models.enums.MediaType;
import com.animeplatform.anime_platform.repositories.MediaRepository;
import com.animeplatform.anime_platform.utils.CheckAndThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Override
    public Page<MediaSummaryDto> getMedia(String search, MediaType type, Pageable pageable) {
        Page<MediaModel> results;

        if (Objects.nonNull(search) && !search.isBlank() && Objects.nonNull(type)) {
            results = mediaRepository.findByMediaTypeAndTitleContainingIgnoreCase(type, search.trim(), pageable);
        } else if (Objects.nonNull(search) && !search.isBlank()) {
            results = mediaRepository.findByTitleContainingIgnoreCase(search.trim(), pageable);
        } else if (Objects.nonNull(type)) {
            results = mediaRepository.findByMediaType(type, pageable);
        } else {
            results = mediaRepository.findAll(pageable);
        }

        return results.map(MediaSummaryDto::fromEntity);
    }
    @Override
    public MediaDetailDto getMediaById(Long id) {
        return MediaDetailDto.fromEntity(findMedia(id));
    }
    @Override
    public MediaDetailDto getMediaBySlug(String slug) {
        MediaModel media = CheckAndThrows.checkOptionalIfNullResourceNotFoundException(
                mediaRepository.findBySlug(slug),
                "No se ha encontrado el título solicitado");
        return MediaDetailDto.fromEntity(media);
    }
    @Override
    @Transactional
    public MediaDetailDto createMedia(MediaRequestDto request) {
        MediaModel media = new MediaModel();
        applyRequest(media, request);

        MediaModel saved = mediaRepository.save(media);
        return MediaDetailDto.fromEntity(saved);
    }
    @Override
    @Transactional
    public MediaDetailDto updateMedia(Long id, MediaRequestDto request) {
        MediaModel media = findMedia(id);
        applyRequest(media, request);

        MediaModel saved = mediaRepository.save(media);
        return MediaDetailDto.fromEntity(saved);
    }

    private MediaModel findMedia(Long id) {
        return CheckAndThrows.checkOptionalIfNullResourceNotFoundException(
                mediaRepository.findById(id),
                "No se ha encontrado el título con ID " + id);
    }

    private void applyRequest(MediaModel media, MediaRequestDto request) {
        media.setMediaType(request.getMediaType());
        media.setTitle(request.getTitle().trim());
        media.setSlug(request.getSlug().trim());
        media.setSynopsis(Optional.ofNullable(request.getSynopsis()).map(String::trim).orElse(null));
        media.setReleaseYear(request.getReleaseYear());
        media.setStatus(request.getStatus());
    }
}
