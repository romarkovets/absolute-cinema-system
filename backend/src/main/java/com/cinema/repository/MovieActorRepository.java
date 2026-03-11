package com.cinema.repository;

import com.cinema.entity.MovieActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieActorRepository extends JpaRepository<MovieActor, Integer> {
    List<MovieActor> findByMovie_MovieId(Integer movieId);
    List<MovieActor> findByActor_ActorId(Integer actorId);
    void deleteByMovie_MovieId(Integer movieId);
}