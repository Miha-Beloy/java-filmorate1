package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        Film created = filmStorage.create(film);
        log.info("Добавлен фильм: {}", created);
        return created;
    }

    public Film updateFilm(Film film) {
        if (film.getId() <= 0) {
            throw new ValidationException("ID должен быть указан для обновления");
        }
        if (!filmStorage.existsById(film.getId())) {
            throw new ValidationException("Фильм с id=" + film.getId() + " не существует");
        }
        validateFilm(film);
        Film updated = filmStorage.update(film);
        log.info("Обновлён фильм: {}", updated);
        return updated;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза {} раньше 28.12.1895", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        // остальная валидация через @Valid в контроллере
    }
}