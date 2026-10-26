package com.animeplatform.anime_platform.dtos;

import com.animeplatform.anime_platform.models.SeasonModel;

public record SeasonResponseDto(
        Long id,
        Long mediaId,
        Integer seasonNumber,
        String title,
        String synopsis,
        Integer releaseYear
) {
    public static SeasonResponseDto from(SeasonModel s) {
        return new SeasonResponseDto(
                s.getId(),
                s.getMedia().getId(),
                s.getSeasonNumber(),
                s.getTitle(),
                s.getSynopsis(),
                s.getReleaseYear()
        );
    }
}