package com.cinema.service;

import com.cinema.dto.MovieDto;
import com.cinema.entity.Movie;
import com.cinema.exception.ResourceNotFoundException;
import com.cinema.mapper.MovieMapper;
import com.cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {

    private static final String MOVIE_NOT_FOUND = "Movie not found with id: ";

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Transactional(readOnly = true)
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovieDto getMovieById(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_NOT_FOUND + id));
        return movieMapper.toDto(movie);
    }

    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = movieMapper.toEntity(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    public MovieDto updateMovie(Integer id, MovieDto movieDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_NOT_FOUND + id));

        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setDurationMinutes(movieDto.getDurationMinutes());
        movie.setReleaseYear(movieDto.getReleaseYear());

        return movieMapper.toDto(movie);
    }

    public void deleteMovie(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_NOT_FOUND + id));
        movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    public List<MovieDto> searchMovies(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }
}