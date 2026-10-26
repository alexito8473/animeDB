package com.animeplatform.anime_platform.dtos;

import com.animeplatform.anime_platform.models.enums.MediaStatus;
import com.animeplatform.anime_platform.models.enums.MediaType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaRequestDto {

    @NotNull(message = "El tipo de contenido es obligatorio")
    private MediaType mediaType;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar 255 caracteres")
    private String title;

    @NotBlank(message = "El slug es obligatorio")
    @Size(max = 255, message = "El slug no puede superar 255 caracteres")
    @Pattern(
            regexp = "[a-z0-9]+(?:-[a-z0-9]+)*",
            message = "El slug debe usar minúsculas, números y guiones"
    )
    private String slug;

    @Size(max = 10000, message = "La sinopsis es demasiado larga")
    private String synopsis;

    @Min(value = 1900, message = "El año debe ser 1900 o posterior")
    @Max(value = 2100, message = "El año no puede superar 2100")
    private Integer releaseYear;

    @NotNull(message = "El estado es obligatorio")
    private MediaStatus status;
}
