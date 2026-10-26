package com.animeplatform.anime_platform.repositories;
import com.animeplatform.anime_platform.models.MediaFileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface MediaFileRepository
        extends JpaRepository<MediaFileModel, Long> {

    Optional<MediaFileModel>
    findFirstByMedia_IdAndFileTypeOrderByIdDesc(
            Long mediaId, String fileType);

    Optional<MediaFileModel>
    findFirstByEpisode_IdAndFileTypeOrderByIdDesc(
            Long episodeId, String fileType);
}