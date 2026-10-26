package com.animeplatform.anime_platform.repositories;

import com.animeplatform.anime_platform.models.SeasonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeasonRepository extends JpaRepository<SeasonModel, Long> {
    List<SeasonModel> findByMedia_IdOrderBySeasonNumberAsc(Long mediaId);
}
