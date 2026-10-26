package com.animeplatform.anime_platform.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "media_file")
public class MediaFileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private MediaModel media;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id")
    private EpisodeModel episode;

    @Column(name = "file_type", nullable = false, length = 30)
    private String fileType;

    @Column(name = "storage_path", nullable = false, unique = true)
    private String storagePath;

    @Column(name = "mime_type")
    private String mimeType;

}
