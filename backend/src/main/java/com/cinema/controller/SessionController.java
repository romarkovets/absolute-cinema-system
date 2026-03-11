package com.cinema.controller;

import com.cinema.dto.SessionDto;
import com.cinema.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    public Page<SessionDto> getAllSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer movieId,
            @RequestParam(required = false) Integer hallId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("sessionDate").ascending().and(Sort.by("startTime")));
        return sessionService.getSessionsWithFilters(date, movieId, hallId, pageable);
    }

    @GetMapping("/{id}")
    public SessionDto getSessionById(@PathVariable Integer id) {
        return sessionService.getSessionById(id);
    }

    @GetMapping("/by-date")
    public Page<SessionDto> getSessionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime"));
        return sessionService.getSessionsByDate(date, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDto createSession(@RequestBody SessionDto sessionDto) {
        return sessionService.createSession(sessionDto);
    }

    @PutMapping("/{id}")
    public SessionDto updateSession(@PathVariable Integer id, @RequestBody SessionDto sessionDto) {
        return sessionService.updateSession(id, sessionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSession(@PathVariable Integer id) {
        sessionService.deleteSession(id);
    }

    @GetMapping("/{id}/sold-tickets")
    public int getSoldTicketsCount(@PathVariable Integer id) {
        return sessionService.getSoldTicketsCount(id);
    }
}