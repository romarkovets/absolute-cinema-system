package com.cinema.controller;

import com.cinema.dto.MovieDto;
import com.cinema.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<MovieDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable Integer id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDto createMovie(@RequestBody MovieDto movieDto) {
        return movieService.createMovie(movieDto);
    }

    @PutMapping("/{id}")
    public MovieDto updateMovie(@PathVariable Integer id, @RequestBody MovieDto movieDto) {
        return movieService.updateMovie(id, movieDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
    }

    @GetMapping("/search")
    public List<MovieDto> searchMovies(@RequestParam String title) {
        return movieService.searchMovies(title);
    }
}