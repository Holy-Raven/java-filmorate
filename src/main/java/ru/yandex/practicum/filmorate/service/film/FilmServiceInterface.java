package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmServiceInterface {

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film delete(Film film);

    Film findFilmById(String filmId);

    Film addLikeFilm(String film, String user);

    Film delLikeFilm(String film, String user);

    List<Film> sortFilm(String count);

}
