package com.cinema.dto;

import lombok.Data;

@Data
public class MovieActorRequest {
    private Integer movieId;
    private Integer actorId;
    private String characterName;
}