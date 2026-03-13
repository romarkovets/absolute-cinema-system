package com.cinema.repository;

import com.cinema.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

    Page<Session> findBySessionDate(LocalDate date, Pageable pageable);

    @Query("SELECT s FROM Session s WHERE s.movie.movieId = :movieId")
    Page<Session> findByMovieId(@Param("movieId") Integer movieId, Pageable pageable);

    @Query("SELECT s FROM Session s WHERE s.hall.hallId = :hallId")
    Page<Session> findByHallId(@Param("hallId") Integer hallId, Pageable pageable);

    @Query("SELECT s FROM Session s WHERE s.sessionDate = :date AND s.movie.movieId = :movieId")
    Page<Session> findByDateAndMovie(@Param("date") LocalDate date, @Param("movieId") Integer movieId, Pageable pageable);

    @Query("SELECT s FROM Session s WHERE s.sessionDate = :date AND s.hall.hallId = :hallId")
    Page<Session> findByDateAndHall(@Param("date") LocalDate date, @Param("hallId") Integer hallId, Pageable pageable);

    @Query("SELECT s FROM Session s WHERE s.movie.movieId = :movieId AND s.hall.hallId = :hallId")
    Page<Session> findByMovieAndHall(@Param("movieId") Integer movieId, @Param("hallId") Integer hallId, Pageable pageable);

    @Query("SELECT s FROM Session s WHERE s.sessionDate = :date AND s.movie.movieId = :movieId AND s.hall.hallId = :hallId")
    Page<Session> findByDateAndMovieAndHall(@Param("date") LocalDate date, @Param("movieId") Integer movieId, @Param("hallId") Integer hallId, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Session s " +
            "WHERE s.hall.hallId = :hallId AND s.sessionDate = :date AND s.startTime = :time")
    boolean existsByHallAndDateAndTime(
            @Param("hallId") Integer hallId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.session.sessionId = :sessionId")
    int countSoldTickets(@Param("sessionId") Integer sessionId);
}