package com.cinema.mapper;

import com.cinema.entity.Session;
import com.cinema.dto.SessionDto;
import com.cinema.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionMapper {
    private final MovieMapper movieMapper;
    private final HallMapper hallMapper;
    private final TicketRepository ticketRepository;

    public SessionDto toDto(Session session) {
        if (session == null) return null;

        SessionDto dto = new SessionDto();
        dto.setSessionId(session.getSessionId());
        dto.setMovie(movieMapper.toDto(session.getMovie()));
        dto.setHall(hallMapper.toDto(session.getHall()));
        dto.setSessionDate(session.getSessionDate());
        dto.setStartTime(session.getStartTime());
        dto.setPrice(session.getPrice());

        int soldTickets = ticketRepository.countBySessionId(session.getSessionId());
        int totalSeats = switch (session.getHall().getHallType()) {
            case 1 -> 120;
            case 2 -> 30;
            case 3 -> 200;
            default -> 0;
        };
        dto.setAvailableSeats(totalSeats - soldTickets);

        return dto;
    }

    public Session toEntity(SessionDto dto) {
        if (dto == null) return null;

        Session session = new Session();
        session.setSessionId(dto.getSessionId());
        session.setSessionDate(dto.getSessionDate());
        session.setStartTime(dto.getStartTime());
        session.setPrice(dto.getPrice());
        return session;
    }
}