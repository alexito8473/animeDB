package com.animeplatform.anime_platform.services.mediaService;

import com.animeplatform.anime_platform.dtos.MediaDetailDto;
import com.animeplatform.anime_platform.dtos.MediaRequestDto;
import com.animeplatform.anime_platform.dtos.MediaSummaryDto;
import com.animeplatform.anime_platform.models.enums.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MediaService {

    Page<MediaSummaryDto> getMedia(String search, MediaType type, Pageable pageable);

    MediaDetailDto getMediaById(Long id);

    MediaDetailDto getMediaBySlug(String slug);

    MediaDetailDto createMedia(MediaRequestDto request);

    MediaDetailDto updateMedia(Long id, MediaRequestDto request);

}
