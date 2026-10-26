package com.animeplatform.anime_platform.controllers;

import com.animeplatform.anime_platform.dtos.SeasonRequestDto;
import com.animeplatform.anime_platform.dtos.SeasonResponseDto;
import com.animeplatform.anime_platform.services.SeasonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SeasonController {

    private final SeasonService service;

    @GetMapping("/media/{mediaId}/seasons")
    public List<SeasonResponseDto> list(@PathVariable Long mediaId) {
        return service.listByMedia(mediaId);
    }

    @GetMapping("/seasons/{id}")
    public SeasonResponseDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping("/seasons")
    @ResponseStatus(HttpStatus.CREATED)
    public SeasonResponseDto create(
            @Valid @RequestBody SeasonRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/seasons/{id}")
    public SeasonResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody SeasonRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/seasons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}