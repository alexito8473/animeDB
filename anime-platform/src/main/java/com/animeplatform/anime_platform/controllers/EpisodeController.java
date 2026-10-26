package com.animeplatform.anime_platform.controllers;

import com.animeplatform.anime_platform.dtos.EpisodeRequestDto;
import com.animeplatform.anime_platform.dtos.EpisodeResponseDto;
import com.animeplatform.anime_platform.services.EpisodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService service;

    @GetMapping("/seasons/{seasonId}/episodes")
    public List<EpisodeResponseDto> list(@PathVariable Long seasonId) {
        return service.listBySeason(seasonId);
    }

    @GetMapping("/episodes/{id}")
    public EpisodeResponseDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping("/episodes")
    @ResponseStatus(HttpStatus.CREATED)
    public EpisodeResponseDto create(
            @Valid @RequestBody EpisodeRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/episodes/{id}")
    public EpisodeResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody EpisodeRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/episodes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
