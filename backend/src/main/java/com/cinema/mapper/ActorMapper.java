package com.cinema.mapper;

import com.cinema.entity.Actor;
import com.cinema.dto.ActorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActorMapper {
    private final MovieActorMapper movieActorMapper;

    public ActorDto toDto(Actor actor) {
        if (actor == null) return null;

        ActorDto dto = new ActorDto();
        dto.setActorId(actor.getActorId());
        dto.setFullName(actor.getFullName());
        dto.setCountry(actor.getCountry());

        if (actor.getMovieActors() != null) {
            dto.setMovieActors(actor.getMovieActors().stream()
                    .map(movieActorMapper::toDto)
                    .toList());
        }

        return dto;
    }

    public Actor toEntity(ActorDto dto) {
        if (dto == null) return null;

        Actor actor = new Actor();
        actor.setActorId(dto.getActorId());
        actor.setFullName(dto.getFullName());
        actor.setCountry(dto.getCountry());
        return actor;
    }
}