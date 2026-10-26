package com.animeplatform.anime_platform.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record EpisodeRequestDto(
        @NotNull @Positive Long seasonId,
        @NotNull @Positive Integer episodeNumber,
        @NotBlank @Size(max = 255) String title,
        @Size(max = 10000) String synopsis,
        @Positive Integer durationSeconds,
        LocalDate airDate,
        @NotBlank String status
) {}