package com.animeplatform.anime_platform.controllers;

import com.animeplatform.anime_platform.services.VideoStreamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/streaming")
@RequiredArgsConstructor
public class VideoController {

    private final VideoStreamingService streamingService;

    @GetMapping("/{slug}")
    public ResponseEntity<InputStreamResource> stream(
            @PathVariable String slug,
            @RequestHeader(value = "Range", required = false) String range) {

        return streamingService.streamBySlug(slug, range);
    }
}
