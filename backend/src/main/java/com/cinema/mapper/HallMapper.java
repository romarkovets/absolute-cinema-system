package com.cinema.mapper;

import com.cinema.entity.Hall;
import com.cinema.dto.HallDto;
import org.springframework.stereotype.Component;

@Component
public class HallMapper {

    public HallDto toDto(Hall hall) {
        if (hall == null) return null;

        HallDto dto = new HallDto();
        dto.setHallId(hall.getHallId());
        dto.setHallNumber(hall.getHallNumber());
        dto.setHallType(hall.getHallType());

        String typeName;
        switch (hall.getHallType()) {
            case 1: typeName = "Обычный"; break;
            case 2: typeName = "VIP"; break;
            case 3: typeName = "IMAX"; break;
            default: typeName = "Неизвестно";
        }
        dto.setHallTypeName(typeName);

        return dto;
    }

    public Hall toEntity(HallDto dto) {
        if (dto == null) return null;

        Hall hall = new Hall();
        hall.setHallId(dto.getHallId());
        hall.setHallNumber(dto.getHallNumber());
        hall.setHallType(dto.getHallType());
        return hall;
    }
}