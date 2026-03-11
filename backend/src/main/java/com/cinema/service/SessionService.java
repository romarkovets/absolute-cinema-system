package com.cinema.service;

import com.cinema.entity.Session;
import com.cinema.dto.SessionDto;
import com.cinema.mapper.SessionMapper;
import com.cinema.exception.ResourceNotFoundException;
import com.cinema.exception.DuplicateSessionException;
import com.cinema.repository.SessionRepository;
import com.cinema.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SessionService {
    private static final String SESSION_NOT_FOUND = "Session not found with id: ";
    private static final String DUPLICATE_SESSION = "Session already exists in hall ";

    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final SessionMapper sessionMapper;

    @Transactional(readOnly = true)
    public Page<SessionDto> getSessionsWithFilters(LocalDate date, Integer movieId, Integer hallId, Pageable pageable) {
        Page<Session> sessionsPage = sessionRepository.findWithFilters(date, movieId, hallId, pageable);
        return sessionsPage.map(sessionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public SessionDto getSessionById(Integer id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SESSION_NOT_FOUND + id));
        return sessionMapper.toDto(session);
    }

    @Transactional(readOnly = true)
    public Page<SessionDto> getSessionsByDate(LocalDate date, Pageable pageable) {
        return sessionRepository.findBySessionDate(date, pageable)
                .map(sessionMapper::toDto);
    }

    @Transactional
    public SessionDto createSession(SessionDto sessionDto) {
        Session session = sessionMapper.toEntity(sessionDto);

        boolean exists = sessionRepository.existsByHallAndDateAndTime(
                session.getHall().getHallId(),
                session.getSessionDate(),
                session.getStartTime()
        );

        if (exists) {
            throw new DuplicateSessionException(
                    DUPLICATE_SESSION + session.getHall().getHallNumber() +
                            " at " + session.getStartTime() + " on " + session.getSessionDate()
            );
        }

        Session saved = sessionRepository.save(session);
        return sessionMapper.toDto(saved);
    }

    @Transactional
    public SessionDto updateSession(Integer id, SessionDto sessionDto) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SESSION_NOT_FOUND + id));

        session.setSessionDate(sessionDto.getSessionDate());
        session.setStartTime(sessionDto.getStartTime());
        session.setPrice(sessionDto.getPrice());

        Session updated = sessionRepository.save(session);
        return sessionMapper.toDto(updated);
    }

    @Transactional
    public void deleteSession(Integer id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SESSION_NOT_FOUND + id));
        sessionRepository.delete(session);
    }

    @Transactional(readOnly = true)
    public int getSoldTicketsCount(Integer sessionId) {
        return sessionRepository.countSoldTickets(sessionId);
    }
}