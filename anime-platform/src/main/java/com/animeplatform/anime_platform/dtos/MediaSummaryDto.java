package com.animeplatform.anime_platform.dtos;


import com.animeplatform.anime_platform.models.MediaModel;
import com.animeplatform.anime_platform.models.enums.MediaStatus;
import com.animeplatform.anime_platform.models.enums.MediaType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MediaSummaryDto {

    private Long id;
    private String title;
    private String slug;
    private MediaType mediaType;
    private Integer releaseYear;
    private MediaStatus status;

    public static MediaSummaryDto fromEntity(MediaModel media) {
        return MediaSummaryDto.builder()
                .id(media.getId())
                .title(media.getTitle())
                .slug(media.getSlug())
                .mediaType(media.getMediaType())
                .releaseYear(media.getReleaseYear())
                .status(media.getStatus())
                .build();
    }
}
