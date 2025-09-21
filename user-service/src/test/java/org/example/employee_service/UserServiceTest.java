package org.example.employee_service;

import org.example.user_service.dto.UserDto;
import org.example.user_service.dto.UserRequestDto;
import org.example.user_service.entity.UserEntity;
import org.example.user_service.exception.UserNotFoundException;
import org.example.user_service.repository.UserRepository;
import org.example.user_service.service.UserKafkaProducer;
import org.example.user_service.service.UserMapper;
import org.example.user_service.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Viktor Shvidkiy
 */
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserKafkaProducer userKafkaProducer;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private UserDto userDto;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        userEntity = UserEntity.builder()
                .id(1)
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .createdAt(LocalDateTime.now())
                .build();

        userDto = UserDto.builder()
                .id(1)
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .createdAt(userEntity.getCreatedAt().toString())
                .build();

        userRequestDto = UserRequestDto.builder()
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .build();
    }

    @Test
    void testCreateUser(){
        when(userMapper.userRequestDtoToEntity(userRequestDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.entityToDto(userEntity)).thenReturn(userDto);

        UserDto result = userService.createUser(userRequestDto);

        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(userDto.getId(), result.getId())
        );
        verify(userRepository).save(userEntity);
    }

    @Test
    void testGetAllUsers(){
        List<UserEntity> users = Arrays.asList(userEntity);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.entityToDto(userEntity)).thenReturn(userDto);

        List<UserDto> result = userService.getAllUsers();

        assertAll(
                ()->assertEquals(1, result.size()),
                ()->assertEquals(userDto.getId(), result.get(0).getId())
        );
        verify(userRepository).findAll();
    }

    @Test
    void testGetAllUsersEmpty(){
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDto> result = userService.getAllUsers();

        assertAll(
                ()->assertNotNull(result),
                ()->assertTrue(result.isEmpty())
        );
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserByIdFound(){
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToDto(userEntity)).thenReturn(userDto);

        UserDto result = userService.getUserById(1);

        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(userDto.getId(), result.getId())
        );
        verify(userRepository).findById(1);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(1)
        );

        assertEquals("Пользователь с ID: 1 не найден.", exception.getMessage());
        verify(userRepository).findById(1);
    }

    @Test
    void testUpdateUserFound(){
        UserRequestDto updateDto = UserRequestDto.builder()
                .name("MrJack")
                .email("ceo_jack@gmail.com")
                .age(21)
                .build();

        UserEntity updatedEntity = UserEntity.builder()
                .id(1)
                .name("MrJack")
                .email("ceo_jack@gmail.com")
                .age(21)
                .createdAt(userEntity.getCreatedAt())
                .build();

        UserDto updatedDto = UserDto.builder()
                .id(1)
                .name("MrJack")
                .email("ceo_jack@gmail.com")
                .age(21)
                .createdAt(updatedEntity.getCreatedAt().toString())
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        doAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            entity.setName(updateDto.getName());
            entity.setEmail(updateDto.getEmail());
            entity.setAge(updateDto.getAge());
            return null;
        }).when(userMapper).updateEntity(userEntity, updateDto);
        when(userRepository.save(userEntity)).thenReturn(updatedEntity);
        when(userMapper.entityToDto(updatedEntity)).thenReturn(updatedDto);

        UserDto result = userService.updateUser(1,updateDto);

        assertAll(
                ()->assertEquals("MrJack", result.getName()),
                ()->assertEquals("ceo_jack@gmail.com", result.getEmail()),
                ()->assertEquals(21,result.getAge())
        );
        verify(userRepository).findById(1);
        verify(userRepository).save(userEntity);
    }

    @Test
    void testUpdateUserNotFound(){
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, ()->{
            userService.updateUser(1,userRequestDto);
        });
        assertEquals("Пользователь с ID: 1 не найден.", exception.getMessage());
        verify(userRepository).findById(1);
    }

    @Test
    void testDeleteUser(){
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(1);
        userService.deleteUser(1);
        verify(userRepository).deleteById(1);
    }
}