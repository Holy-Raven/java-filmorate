package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    public Map<Long, Film> films = new HashMap<>();

    @Override
    public Map<Long, Film> allFilms() {
        return films;
    }

    @Override
    public Film add(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film del(Film film) {
        films.remove(film.getId());
        return film;
    }

    @Override
    public Film findFilmById(Long filmId) {
        return allFilms().get(filmId);
    }

    @Override
    public Film addLikeFilm(Long filmId, Long userId) {
        findFilmById(filmId).getLikes().add(userId);
        return allFilms().get(filmId);
    }

    @Override
    public Film delLikeFilm(Long filmId, Long userId) {

        findFilmById(filmId).getLikes().remove(userId);
        return allFilms().get(filmId);
    }

    @Override
    public List<Film> sortFilm(Long size) {
        return allFilms().values()
               .stream()
               .sorted((film1, film2) -> film2.getLikes().size()
                       - film1.getLikes().size())
               .limit(size)
               .collect(Collectors.toList());
    }
}
