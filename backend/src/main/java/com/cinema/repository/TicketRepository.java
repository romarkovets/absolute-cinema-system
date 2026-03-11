package com.cinema.repository;

import com.cinema.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUser_UserId(Integer userId);
    List<Ticket> findBySession_SessionId(Integer sessionId);

    @Query("SELECT COUNT(t) > 0 FROM Ticket t WHERE t.session.sessionId = :sessionId AND t.seatNumber = :seatNumber")
    boolean isSeatTaken(@Param("sessionId") Integer sessionId, @Param("seatNumber") Integer seatNumber);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.session.sessionId = :sessionId")
    int countBySessionId(@Param("sessionId") Integer sessionId);
}