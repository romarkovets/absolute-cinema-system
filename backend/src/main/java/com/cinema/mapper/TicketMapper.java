package com.cinema.mapper;

import com.cinema.entity.Ticket;
import com.cinema.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketMapper {
    private final SessionMapper sessionMapper;
    private final UserMapper userMapper;

    public TicketDto toDto(Ticket ticket) {
        if (ticket == null) return null;

        TicketDto dto = new TicketDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setSession(sessionMapper.toDto(ticket.getSession()));
        dto.setUser(userMapper.toDto(ticket.getUser()));
        dto.setSeatNumber(ticket.getSeatNumber());
        dto.setPurchaseDate(ticket.getPurchaseDate());

        return dto;
    }

    public Ticket toEntity(TicketDto dto) {
        if (dto == null) return null;

        Ticket ticket = new Ticket();
        ticket.setTicketId(dto.getTicketId());
        ticket.setSeatNumber(dto.getSeatNumber());
        ticket.setPurchaseDate(dto.getPurchaseDate());
        return ticket;
    }
}