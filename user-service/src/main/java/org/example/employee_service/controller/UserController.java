package org.example.employee_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.employee_service.dto.UserDto;
import org.example.employee_service.dto.UserRequestDto;
import org.example.employee_service.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Viktor Shvidkiy
 */
@Tag(name = "Users", description = "Операции с пользователями")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать нового пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(
                            type = "object",
                            additionalPropertiesSchema = String.class,
                            example = "{\"name\":\"Имя не может быть пустым\", \"email\":\"Некорректный email\", \"age\":\"Возраст не может быть отрицательным\"}"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PostMapping
    public EntityModel<UserDto> createUser(@Valid @RequestBody UserRequestDto dto) {
        UserDto createdUser = userService.createUser(dto);
        return toEntityModel(createdUser);
    }

    @Operation(summary = "Получить список всех пользователей")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getAllUsers() {
        List<EntityModel<UserDto>> users = userService.getAllUsers()
                .stream()
                .map(this::toEntityModel)
                .toList();
        return CollectionModel.of(users, WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UserController.class)
                        .getAllUsers())
                .withSelfRel()
        );
    }

    @Operation(summary = "Найти пользователя по ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(
                            type = "object",
                            additionalPropertiesSchema = String.class,
                            example = "{\"name\":\"Имя не может быть пустым\", \"email\":\"Некорректный email\", \"age\":\"Возраст не может быть отрицательным\"}"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public EntityModel<UserDto> getUserById(@PathVariable Integer id) {
        return toEntityModel(userService.getUserById(id));
    }

    @Operation(summary = "Обновить данные пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные успешно обновлены",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public EntityModel<UserDto> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequestDto dto) {
        return toEntityModel(userService.updateUser(id, dto));
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<UserDto> toEntityModel(UserDto user){
        return EntityModel.of(user,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserById(user.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .updateUser(user.getId(), null)).withRel("update"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .deleteUser(user.getId())).withRel("delete"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getAllUsers()).withRel("all-users")
        );
    }
}
