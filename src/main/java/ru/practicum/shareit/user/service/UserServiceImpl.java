package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя"));
    }

    @Override
    public User updateUser(User user, long userId) {
        if (!getUserById(userId).getEmail().equals(user.getEmail())) {
            checkEmail(user);
        }
        User updateUser = getUserById(userId);
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        return userRepository.save(updateUser);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    private Boolean checkEmail(User user) {
        if (userRepository.findAll().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(user.getEmail()))) {
            throw new ConflictException("Такой Email уже существует");
        } else {
            return true;
        }
    }
}