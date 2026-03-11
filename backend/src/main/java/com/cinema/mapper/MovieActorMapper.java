package com.cinema.mapper;

import com.cinema.entity.MovieActor;
import com.cinema.dto.MovieActorDto;
import org.springframework.stereotype.Component;

@Component
public class MovieActorMapper {

    public MovieActorDto toDto(MovieActor movieActor) {
        if (movieActor == null) return null;

        MovieActorDto dto = new MovieActorDto();
        dto.setMovieId(movieActor.getMovie().getMovieId());
        dto.setActorId(movieActor.getActor().getActorId());
        dto.setCharacterName(movieActor.getCharacterName());

        return dto;
    }
}