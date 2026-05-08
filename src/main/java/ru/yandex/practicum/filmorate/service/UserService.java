package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getById(id);
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
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не существует");
        }
        validateUser(user);
        fillEmptyName(user);
        User updated = userStorage.update(user);
        log.info("Обновлён пользователь: {}", updated);
        return updated;
    }

    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }
        checkUserExists(userId);
        checkUserExists(friendId);
        friends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
        log.info("Пользователи {} и {} стали друзьями", userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        checkUserExists(userId);
        checkUserExists(friendId);
        Set<Integer> userFriends = friends.get(userId);
        Set<Integer> friendFriends = friends.get(friendId);
        if (userFriends != null) userFriends.remove(friendId);
        if (friendFriends != null) friendFriends.remove(friendId);
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
    }

    public List<User> getFriends(int userId) {
        checkUserExists(userId);
        Set<Integer> friendIds = friends.getOrDefault(userId, Collections.emptySet());
        return friendIds.stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        checkUserExists(userId);
        checkUserExists(otherId);
        Set<Integer> userFriends = friends.getOrDefault(userId, Collections.emptySet());
        Set<Integer> otherFriends = friends.getOrDefault(otherId, Collections.emptySet());
        Set<Integer> common = new HashSet<>(userFriends);
        common.retainAll(otherFriends);
        return common.stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    private void checkUserExists(int id) {
        if (!userStorage.existsById(id)) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
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