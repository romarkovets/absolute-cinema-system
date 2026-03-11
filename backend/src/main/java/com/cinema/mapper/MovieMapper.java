package com.cinema.mapper;

import com.cinema.dto.MovieDto;
import com.cinema.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieMapper {

    private final ActorMapper actorMapper;

    public MovieDto toDto(Movie movie) {
        if (movie == null) {
            return null;
        }

        MovieDto dto = new MovieDto();
        dto.setMovieId(movie.getMovieId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDurationMinutes(movie.getDurationMinutes());
        dto.setReleaseYear(movie.getReleaseYear());

        if (movie.getMovieActors() != null) {
            dto.setActors(
                    movie.getMovieActors()
                            .stream()
                            .map(ma -> actorMapper.toDto(ma.getActor()))
                            .collect(java.util.stream.Collectors.toSet())
            );
        }

        return dto;
    }

    public Movie toEntity(MovieDto dto) {
        if (dto == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setMovieId(dto.getMovieId());
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDurationMinutes(dto.getDurationMinutes());
        movie.setReleaseYear(dto.getReleaseYear());

        return movie;
    }
}