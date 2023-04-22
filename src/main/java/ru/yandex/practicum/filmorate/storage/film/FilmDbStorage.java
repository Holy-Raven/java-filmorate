package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Override
    public Map<Long, Film> allFilms() {
        return null;
    }

    @Override
    public Film add(Film film) {
        return null;
    }

    @Override
    public Film put(Film film) {
        return null;
    }

    @Override
    public Film del(Film film) {
        return null;
    }

    @Override
    public Film findFilmById(Long filmId) {
        return null;
    }

    @Override
    public Film addLikeFilm(Long film, Long user) {
        return null;
    }

    @Override
    public Film delLikeFilm(Long film, Long user) {
        return null;
    }

    @Override
    public List<Film> sortFilm(Long size) {
        return null;
    }
}
