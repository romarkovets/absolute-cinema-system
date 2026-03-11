package com.cinema.service;

import com.cinema.entity.Ticket;
import com.cinema.dto.TicketDto;
import com.cinema.mapper.TicketMapper;
import com.cinema.exception.ResourceNotFoundException;
import com.cinema.exception.SeatAlreadyTakenException;
import com.cinema.repository.TicketRepository;
import com.cinema.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private static final String TICKET_NOT_FOUND = "Ticket not found with id: ";
    private static final String SEAT_TAKEN = "Seat ";
    private static final String IS_ALREADY_TAKEN = " is already taken";

    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final TicketMapper ticketMapper;

    @Transactional(readOnly = true)
    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketDto getTicketById(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TICKET_NOT_FOUND + id));
        return ticketMapper.toDto(ticket);
    }

    @Transactional
    public TicketDto buyTicket(TicketDto ticketDto) {
        if (ticketDto.getSeatNumber() <= 0) {
            throw new IllegalArgumentException("Seat number must be positive");
        }

        if (ticketRepository.isSeatTaken(ticketDto.getSession().getSessionId(), ticketDto.getSeatNumber())) {
            throw new SeatAlreadyTakenException(SEAT_TAKEN + ticketDto.getSeatNumber() + IS_ALREADY_TAKEN);
        }

        Ticket ticket = ticketMapper.toEntity(ticketDto);
        Ticket saved = ticketRepository.save(ticket);
        return ticketMapper.toDto(saved);
    }

    @Transactional
    public void returnTicket(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TICKET_NOT_FOUND + id));
        ticketRepository.delete(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketDto> getTicketsByUser(Integer userId) {
        return ticketRepository.findByUser_UserId(userId).stream()
                .map(ticketMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TicketDto> getTicketsBySession(Integer sessionId) {
        return ticketRepository.findBySession_SessionId(sessionId).stream()
                .map(ticketMapper::toDto)
                .toList();
    }
}