package com.animeplatform.anime_platform.services.videoStreamingService;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface VideoStreamingService {

    ResponseEntity<InputStreamResource> streamBySlug(String slug, String rangeHeader);

}