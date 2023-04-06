package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Получен запрос 'GET /users'");
        Collection<User> allUsers = userService.getAllUsers();
        return allUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос 'POST /users'");
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.saveUser(user));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info(String.format("Получен запрос 'PATCH /users/{userId}'"), userId);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.updateUser(user, userId));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        log.info(String.format("Получен запрос 'GET /users/{userId}'"), userId);
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info(String.format("Получен запрос 'DELETE /users/{userId}'"), userId);
        userService.deleteUser(userId);
    }
}