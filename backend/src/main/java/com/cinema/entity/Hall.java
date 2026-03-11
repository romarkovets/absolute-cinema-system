package com.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "hall")
@Data
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hallId;

    @Column(name = "hall_number", nullable = false)
    private Integer hallNumber;

    @Column(name = "hall_type", nullable = false)
    private Integer hallType;

    @OneToMany(mappedBy = "hall")
    private Set<Session> sessions;
}