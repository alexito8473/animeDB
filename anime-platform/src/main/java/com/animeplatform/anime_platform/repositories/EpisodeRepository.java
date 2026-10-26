package com.animeplatform.anime_platform.repositories;

import com.animeplatform.anime_platform.models.EpisodeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<EpisodeModel, Long> {
    List<EpisodeModel> findBySeason_IdOrderByEpisodeNumberAsc(Long seasonId);

    Optional<EpisodeModel> findBySlug(String slug);
}