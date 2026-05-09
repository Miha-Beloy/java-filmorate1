package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> getAll();

    User create(User user);

    User update(User user);

    User getById(int id);

    boolean existsById(int id);

    // Методы для друзей
    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    Set<Integer> getFriends(int userId);
}