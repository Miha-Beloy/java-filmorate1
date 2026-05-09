package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    Film getById(int id);

    boolean existsById(int id);

    // Методы для лайков
    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    Set<Integer> getLikes(int filmId);

    // Для получения всех лайков (для топ-фильмов)
    // В данном случае не обязательно выносить в интерфейс, но для чистоты
}