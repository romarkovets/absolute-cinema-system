package com.cinema.controller;

import com.cinema.dto.ActorDto;
import com.cinema.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/actors")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @GetMapping
    public List<ActorDto> getAllActors() {
        return actorService.getAllActors();
    }

    @GetMapping("/{id}")
    public ActorDto getActorById(@PathVariable Integer id) {
        return actorService.getActorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActorDto createActor(@RequestBody ActorDto actorDto) {
        return actorService.createActor(actorDto);
    }

    @PutMapping("/{id}")
    public ActorDto updateActor(@PathVariable Integer id, @RequestBody ActorDto actorDto) {
        return actorService.updateActor(id, actorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(@PathVariable Integer id) {
        actorService.deleteActor(id);
    }
}