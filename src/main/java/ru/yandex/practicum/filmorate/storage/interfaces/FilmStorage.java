package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    Film getById(int id);

    boolean existsById(int id);
}