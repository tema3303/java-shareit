package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User saveUser(User user);

    User getUserById(long userId);

    User updateUser(User user, long userId);

    void deleteUser(long userId);
}