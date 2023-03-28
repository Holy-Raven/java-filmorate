package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> allFilms();

    Film add(Film film);

    Film put(Film film);

    Film del(Film film);

}
