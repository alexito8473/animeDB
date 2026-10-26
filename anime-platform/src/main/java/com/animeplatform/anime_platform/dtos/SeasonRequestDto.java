package com.animeplatform.anime_platform.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SeasonRequestDto(
        @NotNull @Positive Long mediaId,
        @NotNull @Positive Integer seasonNumber,
        @Size(max = 255) String title,
        @Size(max = 10000) String synopsis,
        Integer releaseYear
) {}
