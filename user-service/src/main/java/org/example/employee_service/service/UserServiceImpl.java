package org.example.employee_service.service;

import org.example.commonevents.dto.Operation;
import org.example.commonevents.dto.UserEvent;
import org.example.employee_service.dto.UserDto;
import org.example.employee_service.dto.UserRequestDto;
import org.example.employee_service.entity.UserEntity;
import org.example.employee_service.exception.UserNotFoundException;
import org.example.employee_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Viktor Shvidkiy
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserKafkaProducer kafkaProducer;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserKafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public UserDto createUser(UserRequestDto dto) {
        UserEntity user = userMapper.userRequestDtoToEntity(dto);
        user.setCreatedAt(LocalDateTime.now());
        UserEntity saved = userRepository.save(user);

        kafkaProducer.sendUserEvent(new UserEvent(Operation.CREATE, saved.getEmail()));

        return userMapper.entityToDto(saved);
    }

    @Transactional
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::entityToDto)
                .toList();
    }

    @Transactional
    public UserDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::entityToDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID: " + id + " не найден."));
    }

    @Transactional
    public UserDto updateUser(Integer id, UserRequestDto dto) {
        return userRepository.findById(id)
                .map(user -> {
                    userMapper.updateEntity(user, dto);
                    return userMapper.entityToDto(userRepository.save(user));
                })
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID: " + id + " не найден."));
    }

    @Transactional
    public void deleteUser(Integer id) {
        UserEntity user = userRepository.findById(id)
                        .orElseThrow(()->new UserNotFoundException("Пользователь с ID: " + id + " не найден."));

        userRepository.deleteById(id);

        kafkaProducer.sendUserEvent(new UserEvent(Operation.DELETE, user.getEmail()));
    }
}
