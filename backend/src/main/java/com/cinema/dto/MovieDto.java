package com.cinema.dto;

import lombok.Data;
import java.util.Set;

@Data
public class MovieDto {
    private Integer movieId;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Integer releaseYear;
    private Set<ActorDto> actors;
}