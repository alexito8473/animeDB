package com.animeplatform.anime_platform.services.episodeService;

import com.animeplatform.anime_platform.dtos.EpisodeRequestDto;
import com.animeplatform.anime_platform.dtos.EpisodeResponseDto;

import java.util.List;

public interface EpisodeService {
    List<EpisodeResponseDto> listBySeason(Long seasonId);

    EpisodeResponseDto get(Long id);

    EpisodeResponseDto create(EpisodeRequestDto dto);

    EpisodeResponseDto update(Long id, EpisodeRequestDto dto);

    void delete(Long id);
}