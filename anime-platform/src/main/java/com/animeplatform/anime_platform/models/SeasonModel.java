package com.animeplatform.anime_platform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "season",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_season_media_number",
                columnNames = {"media_id", "season_number"}
        )
)
public class SeasonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_id", nullable = false)
    private MediaModel media;

    @Column(name = "season_number", nullable = false)
    private Integer seasonNumber;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String synopsis;

    @Column(name = "release_year")
    private Integer releaseYear;

}
