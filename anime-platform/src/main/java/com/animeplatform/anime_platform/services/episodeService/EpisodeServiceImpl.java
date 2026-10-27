package com.animeplatform.anime_platform.services.episodeService;

import com.animeplatform.anime_platform.config.exceptions.BadRequestException;
import com.animeplatform.anime_platform.config.exceptions.ResourceNotFoundException;
import com.animeplatform.anime_platform.dtos.EpisodeRequestDto;
import com.animeplatform.anime_platform.dtos.EpisodeResponseDto;
import com.animeplatform.anime_platform.models.EpisodeModel;
import com.animeplatform.anime_platform.models.SeasonModel;
import com.animeplatform.anime_platform.repositories.EpisodeRepository;
import com.animeplatform.anime_platform.repositories.SeasonRepository;
import com.animeplatform.anime_platform.utils.CheckAndThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class EpisodeServiceImpl implements EpisodeService {

    private static final Set<String> VALID_STATUSES =
            Set.of("ACTIVE", "INACTIVE", "UPCOMING");
    @Autowired
    private EpisodeRepository episodes;
    @Autowired
    private SeasonRepository seasons;

    @Override
    @Transactional(readOnly = true)
    public List<EpisodeResponseDto> listBySeason(Long seasonId) {
        if (!seasons.existsById(seasonId)) {
            throw new ResourceNotFoundException("Temporada no encontrada");
        }
        return episodes.findBySeason_IdOrderByEpisodeNumberAsc(seasonId)
                .stream().map(EpisodeResponseDto::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EpisodeResponseDto get(Long id) {
        return EpisodeResponseDto.from(find(id));
    }

    @Override
    public EpisodeResponseDto create(EpisodeRequestDto dto) {
        EpisodeModel e = new EpisodeModel();
        apply(e, dto);
        return EpisodeResponseDto.from(episodes.save(e));
    }

    @Override
    public EpisodeResponseDto update(Long id, EpisodeRequestDto dto) {
        EpisodeModel e = find(id);
        apply(e, dto);
        return EpisodeResponseDto.from(episodes.save(e));
    }

    @Override
    public void delete(Long id) {
        episodes.delete(find(id));
    }


    private EpisodeModel find(Long id) {
        return CheckAndThrows.checkOptionalIfNullResourceNotFoundException(episodes.findById(id), "Episodio no encontrado");
    }

    private void apply(EpisodeModel e, EpisodeRequestDto dto) {
        if (Objects.isNull(dto.status()) || !VALID_STATUSES.contains(dto.status())) {
            throw new BadRequestException("Estado inválido. Usa ACTIVE, INACTIVE o UPCOMING");
        }
        SeasonModel season = CheckAndThrows.checkOptionalIfNullResourceNotFoundException(
                seasons.findById(dto.seasonId()),
                "Temporada no encontrada");

        e.setSeason(season);
        e.setEpisodeNumber(dto.episodeNumber());
        e.setTitle(dto.title().trim());
        e.setSynopsis(dto.synopsis());
        e.setDurationSeconds(dto.durationSeconds());
        e.setAirDate(dto.airDate());
        e.setStatus(dto.status());
    }
}