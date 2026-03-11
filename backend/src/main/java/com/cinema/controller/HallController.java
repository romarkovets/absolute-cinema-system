package com.cinema.controller;

import com.cinema.dto.HallDto;
import com.cinema.mapper.HallMapper;
import com.cinema.repository.HallRepository;
import com.cinema.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
public class HallController {
    private static final String HALL_NOT_FOUND = "Hall not found with id: ";

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;

    @GetMapping
    public List<HallDto> getAllHalls() {
        return hallRepository.findAll().stream()
                .map(hallMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public HallDto getHallById(@PathVariable Integer id) {
        return hallRepository.findById(id)
                .map(hallMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(HALL_NOT_FOUND + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HallDto createHall(@RequestBody HallDto hallDto) {
        var hall = hallMapper.toEntity(hallDto);
        var saved = hallRepository.save(hall);
        return hallMapper.toDto(saved);
    }

    @PutMapping("/{id}")
    public HallDto updateHall(@PathVariable Integer id, @RequestBody HallDto hallDto) {
        var hall = hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HALL_NOT_FOUND + id));
        hall.setHallNumber(hallDto.getHallNumber());
        hall.setHallType(hallDto.getHallType());
        var updated = hallRepository.save(hall);
        return hallMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHall(@PathVariable Integer id) {
        var hall = hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HALL_NOT_FOUND + id));
        hallRepository.delete(hall);
    }
}