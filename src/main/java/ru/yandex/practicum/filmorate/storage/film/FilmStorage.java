package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> allFilms();

    Film add(Film film);

    void put(Film film);

    void del(Film film);

    Optional<Film> findFilmById(Long filmId);

    void addGenreToFilm(long filmId, long genreId);

    List<Genre> findGenreListFilmById(long id);

    void delGenresListFromFilm(long filmId);

    boolean existsById(Long id);
}
