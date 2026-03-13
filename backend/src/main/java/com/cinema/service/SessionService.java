package com.cinema.service;

import com.cinema.entity.Session;
import com.cinema.dto.SessionDto;
import com.cinema.mapper.SessionMapper;
import com.cinema.exception.ResourceNotFoundException;
import com.cinema.exception.DuplicateSessionException;
import com.cinema.repository.SessionRepository;
import com.cinema.repository.TicketRepository;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.HallRepository;
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
    private static final String MOVIE_NOT_FOUND = "Movie not found with id: ";
    private static final String HALL_NOT_FOUND = "Hall not found with id: ";
    private static final String DUPLICATE_SESSION = "Session already exists in hall ";

    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final SessionMapper sessionMapper;

    @Transactional(readOnly = true)
    public Page<SessionDto> getSessionsWithFilters(LocalDate date, Integer movieId, Integer hallId, Pageable pageable) {
        Page<Session> sessionsPage;

        if (date != null && movieId != null && hallId != null) {
            sessionsPage = sessionRepository.findByDateAndMovieAndHall(date, movieId, hallId, pageable);
        } else if (date != null && movieId != null) {
            sessionsPage = sessionRepository.findByDateAndMovie(date, movieId, pageable);
        } else if (date != null && hallId != null) {
            sessionsPage = sessionRepository.findByDateAndHall(date, hallId, pageable);
        } else if (movieId != null && hallId != null) {
            sessionsPage = sessionRepository.findByMovieAndHall(movieId, hallId, pageable);
        } else if (date != null) {
            sessionsPage = sessionRepository.findBySessionDate(date, pageable);
        } else if (movieId != null) {
            sessionsPage = sessionRepository.findByMovieId(movieId, pageable);
        } else if (hallId != null) {
            sessionsPage = sessionRepository.findByHallId(hallId, pageable);
        } else {
            sessionsPage = sessionRepository.findAll(pageable);
        }

        return sessionsPage.map(session -> {
            SessionDto dto = sessionMapper.toDto(session);
            int soldTickets = ticketRepository.countBySessionId(session.getSessionId());
            int totalSeats = switch (session.getHall().getHallType()) {
                case 1 -> 120;
                case 2 -> 30;
                case 3 -> 200;
                default -> 0;
            };
            dto.setAvailableSeats(totalSeats - soldTickets);
            return dto;
        });
    }

    @Transactional(readOnly = true)
    public SessionDto getSessionById(Integer id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SESSION_NOT_FOUND + id));
        SessionDto dto = sessionMapper.toDto(session);
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

    @Transactional
    public SessionDto createSession(SessionDto sessionDto) {
        Session session = sessionMapper.toEntity(sessionDto);

        var movie = movieRepository.findById(sessionDto.getMovie().getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_NOT_FOUND + sessionDto.getMovie().getMovieId()));
        var hall = hallRepository.findById(sessionDto.getHall().getHallId())
                .orElseThrow(() -> new ResourceNotFoundException(HALL_NOT_FOUND + sessionDto.getHall().getHallId()));

        session.setMovie(movie);
        session.setHall(hall);

        boolean exists = sessionRepository.existsByHallAndDateAndTime(
                hall.getHallId(),
                session.getSessionDate(),
                session.getStartTime()
        );

        if (exists) {
            throw new DuplicateSessionException(
                    DUPLICATE_SESSION + hall.getHallNumber() +
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

        var movie = movieRepository.findById(sessionDto.getMovie().getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_NOT_FOUND + sessionDto.getMovie().getMovieId()));
        var hall = hallRepository.findById(sessionDto.getHall().getHallId())
                .orElseThrow(() -> new ResourceNotFoundException(HALL_NOT_FOUND + sessionDto.getHall().getHallId()));

        session.setMovie(movie);
        session.setHall(hall);
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