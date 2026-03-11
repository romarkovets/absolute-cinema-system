package com.cinema.repository;

import com.cinema.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    List<Actor> findByFullNameContainingIgnoreCase(String name);
    List<Actor> findByCountry(String country);
}