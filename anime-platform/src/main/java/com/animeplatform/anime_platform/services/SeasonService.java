package com.animeplatform.anime_platform.services;


import com.animeplatform.anime_platform.dtos.SeasonRequestDto;
import com.animeplatform.anime_platform.dtos.SeasonResponseDto;
import com.animeplatform.anime_platform.models.MediaModel;
import com.animeplatform.anime_platform.models.SeasonModel;
import com.animeplatform.anime_platform.repositories.MediaRepository;
import com.animeplatform.anime_platform.repositories.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeasonService {

    private final SeasonRepository seasons;
    private final MediaRepository mediaRepository;

    @Transactional(readOnly = true)
    public List<SeasonResponseDto> listByMedia(Long mediaId) {
        if (!mediaRepository.existsById(mediaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe ese contenido");
        }
        return seasons.findByMedia_IdOrderBySeasonNumberAsc(mediaId)
                .stream().map(SeasonResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public SeasonResponseDto get(Long id) {
        return SeasonResponseDto.from(find(id));
    }

    public SeasonResponseDto create(SeasonRequestDto dto) {
        SeasonModel s = new SeasonModel();
        apply(s, dto);
        return SeasonResponseDto.from(seasons.save(s));
    }

    public SeasonResponseDto update(Long id, SeasonRequestDto dto) {
        SeasonModel s = find(id);
        apply(s, dto);
        return SeasonResponseDto.from(seasons.save(s));
    }

    public void delete(Long id) {
        SeasonModel s = find(id);
        if (!s.getMedia().getId().equals(
                s.getMedia().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        seasons.delete(s);
    }

    private SeasonModel find(Long id) {
        return seasons.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Temporada no encontrada"));
    }

    private void apply(SeasonModel s, SeasonRequestDto dto) {
        MediaModel media = mediaRepository.findById(dto.mediaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Anime o película no encontrado"));

        s.setMedia(media);
        s.setSeasonNumber(dto.seasonNumber());
        s.setTitle(dto.title());
        s.setSynopsis(dto.synopsis());
        s.setReleaseYear(dto.releaseYear());
    }
}