package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> allFilms();

    Collection<Long> keyFilms();

    Film add(Film film);

    Film put(Film film);

    Film del(Film film);

}
