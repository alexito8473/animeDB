package com.animeplatform.anime_platform.services.seasonService;


import com.animeplatform.anime_platform.dtos.SeasonRequestDto;
import com.animeplatform.anime_platform.dtos.SeasonResponseDto;

import java.util.List;

public interface SeasonService {

    List<SeasonResponseDto> listByMedia(Long mediaId);

    SeasonResponseDto get(Long id);

    SeasonResponseDto create(SeasonRequestDto dto);

    SeasonResponseDto update(Long id, SeasonRequestDto dto);

    void delete(Long id);

}