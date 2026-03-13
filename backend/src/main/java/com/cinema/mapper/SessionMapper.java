package com.cinema.mapper;

import com.cinema.entity.Session;
import com.cinema.dto.SessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionMapper {
    private final MovieMapper movieMapper;
    private final HallMapper hallMapper;

    public SessionDto toDto(Session session) {
        if (session == null) return null;

        SessionDto dto = new SessionDto();
        dto.setSessionId(session.getSessionId());
        dto.setMovie(movieMapper.toDto(session.getMovie()));
        dto.setHall(hallMapper.toDto(session.getHall()));
        dto.setSessionDate(session.getSessionDate());
        dto.setStartTime(session.getStartTime());
        dto.setPrice(session.getPrice());
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