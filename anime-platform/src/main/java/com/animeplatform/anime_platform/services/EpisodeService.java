package com.animeplatform.anime_platform.services;

import com.animeplatform.anime_platform.dtos.EpisodeRequestDto;
import com.animeplatform.anime_platform.dtos.EpisodeResponseDto;
import com.animeplatform.anime_platform.models.EpisodeModel;
import com.animeplatform.anime_platform.models.SeasonModel;
import com.animeplatform.anime_platform.repositories.EpisodeRepository;
import com.animeplatform.anime_platform.repositories.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class EpisodeService {

    private final EpisodeRepository episodes;
    private final SeasonRepository seasons;

    private static final Set<String> VALID_STATUSES =
            Set.of("ACTIVE", "INACTIVE", "UPCOMING");

    @Transactional(readOnly = true)
    public List<EpisodeResponseDto> listBySeason(Long seasonId) {
        if (!seasons.existsById(seasonId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Temporada no encontrada");
        }
        return episodes.findBySeason_IdOrderByEpisodeNumberAsc(seasonId)
                .stream().map(EpisodeResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public EpisodeResponseDto get(Long id) {
        return EpisodeResponseDto.from(find(id));
    }

    public EpisodeResponseDto create(EpisodeRequestDto dto) {
        EpisodeModel e = new EpisodeModel();
        apply(e, dto);
        return EpisodeResponseDto.from(episodes.save(e));
    }

    public EpisodeResponseDto update(Long id, EpisodeRequestDto dto) {
        EpisodeModel e = find(id);
        apply(e, dto);
        return EpisodeResponseDto.from(episodes.save(e));
    }

    public void delete(Long id) {
        episodes.delete(find(id));
    }

    private EpisodeModel find(Long id) {
        return episodes.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Episodio no encontrado"));
    }

    private void apply(EpisodeModel e, EpisodeRequestDto dto) {
        if (dto.status() == null ||
                !VALID_STATUSES.contains(dto.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estado inválido. Usa ACTIVE, INACTIVE o UPCOMING");
        }

        SeasonModel season = seasons.findById(dto.seasonId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Temporada no encontrada"));

        e.setSeason(season);
        e.setEpisodeNumber(dto.episodeNumber());
        e.setTitle(dto.title().trim());
        e.setSynopsis(dto.synopsis());
        e.setDurationSeconds(dto.durationSeconds());
        e.setAirDate(dto.airDate());
        e.setStatus(dto.status());
    }
}