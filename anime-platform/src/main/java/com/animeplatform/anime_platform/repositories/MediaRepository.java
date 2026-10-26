package com.animeplatform.anime_platform.repositories;

import com.animeplatform.anime_platform.models.MediaModel;
import com.animeplatform.anime_platform.models.enums.MediaStatus;
import com.animeplatform.anime_platform.models.enums.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<MediaModel, Long> {

    Optional<MediaModel> findBySlug(String slug);

    Page<MediaModel> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<MediaModel> findByMediaType(MediaType mediaType, Pageable pageable);

    Page<MediaModel> findByStatus(MediaStatus status, Pageable pageable);

    Page<MediaModel> findByMediaTypeAndTitleContainingIgnoreCase(MediaType mediaType, String title, Pageable pageable);
}
