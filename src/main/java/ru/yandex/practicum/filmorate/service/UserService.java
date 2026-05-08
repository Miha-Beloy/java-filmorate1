package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        validateUser(user);
        fillEmptyName(user);
        User created = userStorage.create(user);
        log.info("Добавлен пользователь: {}", created);
        return created;
    }

    public User updateUser(User user) {
        if (user.getId() <= 0) {
            throw new ValidationException("ID должен быть указан для обновления");
        }
        if (!userStorage.existsById(user.getId())) {
            throw new ValidationException("Пользователь с id=" + user.getId() + " не существует");
        }
        validateUser(user);
        fillEmptyName(user);
        User updated = userStorage.update(user);
        log.info("Обновлён пользователь: {}", updated);
        return updated;
    }

    private void validateUser(User user) {
        if (user.getLogin() != null && user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private void fillEmptyName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}