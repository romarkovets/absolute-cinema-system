package com.cinema.controller;

import com.cinema.dto.TicketDto;
import com.cinema.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public List<TicketDto> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public TicketDto getTicketById(@PathVariable Integer id) {
        return ticketService.getTicketById(id);
    }

    @PostMapping("/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto buyTicket(@RequestBody TicketDto ticketDto) {
        return ticketService.buyTicket(ticketDto);
    }

    @PostMapping("/{id}/return")
    public void returnTicket(@PathVariable Integer id) {
        ticketService.returnTicket(id);
    }

    @GetMapping("/user/{userId}")
    public List<TicketDto> getTicketsByUser(@PathVariable Integer userId) {
        return ticketService.getTicketsByUser(userId);
    }

    @GetMapping("/session/{sessionId}")
    public List<TicketDto> getTicketsBySession(@PathVariable Integer sessionId) {
        return ticketService.getTicketsBySession(sessionId);
    }
}