package com.cinema.service;

import com.cinema.entity.Actor;
import com.cinema.dto.ActorDto;
import com.cinema.mapper.ActorMapper;
import com.cinema.exception.ResourceNotFoundException;
import com.cinema.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {
    private static final String ACTOR_NOT_FOUND = "Actor not found with id: ";

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;

    @Transactional(readOnly = true)
    public List<ActorDto> getAllActors() {
        return actorRepository.findAll().stream()
                .map(actorMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ActorDto getActorById(Integer id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACTOR_NOT_FOUND + id));
        return actorMapper.toDto(actor);
    }

    @Transactional
    public ActorDto createActor(ActorDto actorDto) {
        Actor actor = actorMapper.toEntity(actorDto);
        Actor saved = actorRepository.save(actor);
        return actorMapper.toDto(saved);
    }

    @Transactional
    public ActorDto updateActor(Integer id, ActorDto actorDto) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACTOR_NOT_FOUND + id));

        actor.setFullName(actorDto.getFullName());
        actor.setCountry(actorDto.getCountry());

        Actor updated = actorRepository.save(actor);
        return actorMapper.toDto(updated);
    }

    @Transactional
    public void deleteActor(Integer id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACTOR_NOT_FOUND + id));
        actorRepository.delete(actor);
    }
}