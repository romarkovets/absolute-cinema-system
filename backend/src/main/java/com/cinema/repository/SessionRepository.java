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
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

    @Query("SELECT s FROM Session s WHERE " +
            "(:date IS NULL OR s.sessionDate = :date) AND " +
            "(:movieId IS NULL OR s.movie.movieId = :movieId) AND " +
            "(:hallId IS NULL OR s.hall.hallId = :hallId)")
    Page<Session> findWithFilters(
            @Param("date") LocalDate date,
            @Param("movieId") Integer movieId,
            @Param("hallId") Integer hallId,
            Pageable pageable);

    Page<Session> findBySessionDate(LocalDate date, Pageable pageable);

    List<Session> findBySessionDate(LocalDate date);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Session s " +
            "WHERE s.hall.hallId = :hallId AND s.sessionDate = :date AND s.startTime = :time")
    boolean existsByHallAndDateAndTime(
            @Param("hallId") Integer hallId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time);

    @Query("SELECT s FROM Session s WHERE s.hall.hallId = :hallId AND s.sessionDate = :date")
    List<Session> findByHallAndDate(@Param("hallId") Integer hallId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.session.sessionId = :sessionId")
    int countSoldTickets(@Param("sessionId") Integer sessionId);
}