package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> allFilms();

    Film add(Film film);

    void put(Film film);

    void del(Film film);

    Optional<Film> findFilmById(Long filmId);

    Film addLikeFilm(Long film, Long user);

    Film delLikeFilm(Long film, Long user);

    List<Film> sortFilm(Long size);

    boolean existsById(Long id);
}
