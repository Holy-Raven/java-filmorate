package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> allFilms();

    Film add(Film film);

    Film put(Film film);

    Film del(Film film);

    Film findFilmById(Long filmId);

    Film addLikeFilm(Long film, Long user);

    Film delLikeFilm(Long film, Long user);

    List<Film> sortFilm(Long size);

}
