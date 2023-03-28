package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmServiceInterface {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film delete(Film film);

}
