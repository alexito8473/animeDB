package com.animeplatform.anime_platform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "episode",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_episode_season_number",
                columnNames = {"season_id", "episode_number"}
        )
)
public class EpisodeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private SeasonModel season;

    @Column(name = "episode_number", nullable = false)
    private Integer episodeNumber;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String synopsis;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "air_date")
    private LocalDate airDate;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "slug")
    private String slug;
}
