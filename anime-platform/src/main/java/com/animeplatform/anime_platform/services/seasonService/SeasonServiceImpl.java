package com.animeplatform.anime_platform.services.seasonService;

import com.animeplatform.anime_platform.config.exceptions.BadRequestException;
import com.animeplatform.anime_platform.dtos.SeasonRequestDto;
import com.animeplatform.anime_platform.dtos.SeasonResponseDto;
import com.animeplatform.anime_platform.models.MediaModel;
import com.animeplatform.anime_platform.models.SeasonModel;
import com.animeplatform.anime_platform.repositories.MediaRepository;
import com.animeplatform.anime_platform.repositories.SeasonRepository;
import com.animeplatform.anime_platform.utils.CheckAndThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SeasonServiceImpl implements SeasonService {

    @Autowired
    private SeasonRepository seasons;

    @Autowired
    private MediaRepository mediaRepository;

    @Override
    public List<SeasonResponseDto> listByMedia(Long mediaId) {
        if (!mediaRepository.existsById(mediaId)) {
            throw new BadRequestException("No existe ese contenido");
        }
        return seasons.findByMedia_IdOrderBySeasonNumberAsc(mediaId)
                .stream().map(SeasonResponseDto::from).toList();
    }

    @Override
    public SeasonResponseDto get(Long id) {
        return SeasonResponseDto.from(find(id));
    }

    @Transactional
    @Override
    public SeasonResponseDto create(SeasonRequestDto dto) {
        SeasonModel s = new SeasonModel();
        apply(s, dto);
        return SeasonResponseDto.from(seasons.save(s));
    }

    @Transactional
    @Override
    public SeasonResponseDto update(Long id, SeasonRequestDto dto) {
        SeasonModel s = find(id);
        apply(s, dto);
        return SeasonResponseDto.from(seasons.save(s));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        SeasonModel s = find(id);
        if (!s.getMedia().getId().equals(s.getMedia().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        seasons.delete(s);
    }

    private SeasonModel find(Long id) {
        return CheckAndThrows.checkOptionalIfNullResourceNotFoundException(seasons.findById(id), "Temporada no encontrada");
    }

    private void apply(SeasonModel s, SeasonRequestDto dto) {
        MediaModel media = CheckAndThrows.checkOptionalIfNullResourceNotFoundException(
                mediaRepository.findById(dto.mediaId()),
                "Anime o película no encontrado");

        s.setMedia(media);
        s.setSeasonNumber(dto.seasonNumber());
        s.setTitle(dto.title());
        s.setSynopsis(dto.synopsis());
        s.setReleaseYear(dto.releaseYear());
    }
}