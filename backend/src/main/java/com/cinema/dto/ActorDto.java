package com.cinema.dto;

import lombok.Data;
import java.util.List;

@Data
public class ActorDto {
    private Integer actorId;
    private String fullName;
    private String country;
    private List<MovieActorDto> movieActors;
}