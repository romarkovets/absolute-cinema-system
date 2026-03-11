package com.cinema.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MovieActorId implements Serializable {

    private Integer movie;
    private Integer actor;
}