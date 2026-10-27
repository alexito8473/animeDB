package com.animeplatform.anime_platform.controllers;


import com.animeplatform.anime_platform.dtos.MediaDetailDto;
import com.animeplatform.anime_platform.dtos.MediaRequestDto;
import com.animeplatform.anime_platform.dtos.MediaSummaryDto;
import com.animeplatform.anime_platform.models.enums.MediaType;
import com.animeplatform.anime_platform.services.mediaService.MediaService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping
    public Page<MediaSummaryDto> getMedia(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) MediaType type,
            @PageableDefault(
                    size = 20,
                    sort = "title",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return mediaService.getMedia(search, type, pageable);
    }

    @GetMapping("/{id}")
    public MediaDetailDto getMediaById(@PathVariable Long id) {
        return mediaService.getMediaById(id);
    }

    @GetMapping("/slug/{slug}")
    public MediaDetailDto getMediaBySlug(@PathVariable String slug) {
        return mediaService.getMediaBySlug(slug);
    }

    @PostMapping
    public ResponseEntity<MediaDetailDto> createMedia(@Valid @RequestBody MediaRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaService.createMedia(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaDetailDto> updateMedia(@PathVariable Long id, @Valid @RequestBody MediaRequestDto request) {
        return ResponseEntity.ok(mediaService.updateMedia(id, request));
    }
}
