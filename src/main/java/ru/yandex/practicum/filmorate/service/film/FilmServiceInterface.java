package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface FilmServiceInterface {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film delete(Film film);

    Film findFilmById(String filmId);

}
