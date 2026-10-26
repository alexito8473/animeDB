package com.animeplatform.anime_platform.dtos;

import com.animeplatform.anime_platform.models.EpisodeModel;
import java.time.LocalDate;

public record EpisodeResponseDto(
        Long id,
        Long seasonId,
        Integer episodeNumber,
        String title,
        String synopsis,
        Integer durationSeconds,
        LocalDate airDate,
        String status
) {
    public static EpisodeResponseDto from(EpisodeModel e) {
        return new EpisodeResponseDto(
                e.getId(),
                e.getSeason().getId(),
                e.getEpisodeNumber(),
                e.getTitle(),
                e.getSynopsis(),
                e.getDurationSeconds(),
                e.getAirDate(),
                e.getStatus()
        );
    }
}