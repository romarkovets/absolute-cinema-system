package com.cinema.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "actor")
@Getter
@Setter
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer actorId;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    private String country;

    @OneToMany(mappedBy = "actor")
    private Set<MovieActor> movieActors;
}