package com.cinema.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer userId;
    private String email;
    private String fullName;
    private String phone;
}