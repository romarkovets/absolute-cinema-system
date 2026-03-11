package com.cinema.service;

import com.cinema.entity.User;
import com.cinema.dto.UserDto;
import com.cinema.mapper.UserMapper;
import com.cinema.exception.ResourceNotFoundException;
import com.cinema.exception.DuplicateResourceException;
import com.cinema.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_NOT_FOUND = "User not found with id: ";
    private static final String USER_NOT_FOUND_EMAIL = "User not found with email: ";
    private static final String EMAIL_EXISTS = "Email already exists: ";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + id));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException(EMAIL_EXISTS + userDto.getEmail());
        }

        User user = userMapper.toEntity(userDto);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public UserDto updateUser(Integer id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + id));

        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());

        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + id));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_EMAIL + email));
        return userMapper.toDto(user);
    }
}