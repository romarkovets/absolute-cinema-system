package com.cinema.controller;

import com.cinema.entity.MovieActor;
import com.cinema.dto.MovieActorRequest;
import com.cinema.dto.MovieActorDto;
import com.cinema.mapper.MovieActorMapper;
import com.cinema.repository.MovieActorRepository;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.ActorRepository;
import com.cinema.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movie-actors")
@RequiredArgsConstructor
public class MovieActorController {
    private static final String MOVIE_ACTOR_NOT_FOUND = "MovieActor relation not found with id: ";
    private static final String MOVIE_NOT_FOUND = "Movie not found with id: ";
    private static final String ACTOR_NOT_FOUND = "Actor not found with id: ";
    private static final String ACTOR_ALREADY_LINKED = "This actor is already linked to the movie";

    private final MovieActorRepository movieActorRepository;
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieActorMapper movieActorMapper;

    @GetMapping("/movie/{movieId}")
    public List<MovieActorDto> getActorsByMovie(@PathVariable Integer movieId) {
        return movieActorRepository.findByMovie_MovieId(movieId).stream()
                .map(movieActorMapper::toDto)
                .toList();
    }

    @GetMapping("/actor/{actorId}")
    public List<MovieActorDto> getMoviesByActor(@PathVariable Integer actorId) {
        return movieActorRepository.findByActor_ActorId(actorId).stream()
                .map(movieActorMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public MovieActorDto getById(@PathVariable Integer id) {
        MovieActor movieActor = movieActorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_ACTOR_NOT_FOUND + id));
        return movieActorMapper.toDto(movieActor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieActorDto addActorToMovie(@RequestBody MovieActorRequest request) {
        var movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_NOT_FOUND + request.getMovieId()));
        var actor = actorRepository.findById(request.getActorId())
                .orElseThrow(() -> new ResourceNotFoundException(ACTOR_NOT_FOUND + request.getActorId()));

        List<MovieActor> existing = movieActorRepository.findByMovie_MovieId(request.getMovieId());
        boolean alreadyExists = existing.stream()
                .anyMatch(ma -> ma.getActor().getActorId().equals(request.getActorId()));

        if (alreadyExists) {
            throw new IllegalArgumentException(ACTOR_ALREADY_LINKED);
        }

        MovieActor movieActor = new MovieActor();
        movieActor.setMovie(movie);
        movieActor.setActor(actor);
        movieActor.setCharacterName(request.getCharacterName());

        MovieActor saved = movieActorRepository.save(movieActor);
        return movieActorMapper.toDto(saved);
    }

    @PutMapping("/{id}")
    public MovieActorDto updateCharacterName(@PathVariable Integer id, @RequestBody String characterName) {
        MovieActor movieActor = movieActorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_ACTOR_NOT_FOUND + id));
        movieActor.setCharacterName(characterName);
        MovieActor updated = movieActorRepository.save(movieActor);
        return movieActorMapper.toDto(updated);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeActorFromMovie(
            @RequestParam Integer movieId,
            @RequestParam Integer actorId) {
        List<MovieActor> relations = movieActorRepository.findByMovie_MovieId(movieId);
        relations.stream()
                .filter(ma -> ma.getActor().getActorId().equals(actorId))
                .findFirst()
                .ifPresent(movieActorRepository::delete);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        MovieActor movieActor = movieActorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_ACTOR_NOT_FOUND + id));
        movieActorRepository.delete(movieActor);
    }
}