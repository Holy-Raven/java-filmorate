package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BusinessLogicException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.Constant;

import java.util.*;

@Service
@Slf4j
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;

//    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
//        this.filmStorage = filmStorage;
//    }

    public FilmService(@Qualifier("InMemoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }
    private long newId = 1;

    private long getNewId() {
        return newId++;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(filmStorage.allFilms().values());
    }

    @Override
    public Film create(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Name of the film cannot be empty");
            throw new ValidationException("Name of the film cannot be empty");

        } else if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("The maximum length of the description should not exceed 200 characters");
            throw new ValidationException("The maximum length of the description should not exceed 200 characters");

        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(Constant.BEGIN_TIME)) {
            log.error("The release date may not be earlier than December 28, 1895");
            throw new ValidationException("The release date may not be earlier than December 28, 1895");

        } else if (film.getDuration() != null && film.getDuration() <= 0) {
            log.error("The duration of the film should be positive");
            throw new ValidationException("The duration of the film should be positive");

        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(Constant.BEGIN_TIME)) {
            log.error("The release date may not be earlier than December 28, 1895");
            throw new ValidationException("The release date may not be earlier than December 28, 1895");
        }

        film = new Film(getNewId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());

        log.info("Added Film: {}", film.getName());
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {

        if (filmStorage.allFilms().containsKey(film.getId())) {
            filmStorage.put(film);
        } else {
            log.error("There is no such film in our list of films");
            throw new FilmNotFoundException("There is no such film in our list of films");
        }

        log.info("Updated movie description: {}", film.getName());
        return film;

    }

    @Override
    public Film delete(Film film) {

        if (filmStorage.allFilms().containsKey(film.getId())) {
            filmStorage.del(film);
        } else {
            log.error("There is no such film in our list of films");
            throw new FilmNotFoundException("There is no such film in our list of films");
        }

        log.info("Movie deleted {}", film.getName());
        return film;

    }

    @Override
    public Film findFilmById(String filmId) {

        long id = parseStringInLong(filmId);

        if (filmStorage.allFilms().containsKey(id)) {
            return filmStorage.findFilmById(id);
        } else {
            throw new FilmNotFoundException("There is no such film in our list of films");
        }
    }

    @Override
    public List<Film> sortFilm(String count) {

        long size = parseStringInLong(count);

        log.info("Cписок фильмов отсортированный по их популярности");
        return filmStorage.sortFilm(size);
    }

    @Override
    public Film addLikeFilm(String film, String user) {

        long filmId = parseStringInLong(film);
        long userId = parseStringInLong(user);

        if (filmStorage.allFilms().get(filmId).getLikes().contains(userId)) {
            log.error("The user has already liked this movie");
            throw new BusinessLogicException("The user has already liked this movie");
        }

        log.info("User №" + userId + " I liked the movie №" + filmId);

        return filmStorage.addLikeFilm(filmId,userId);
    }

    @Override
    public Film delLikeFilm(String film, String user) {

        long filmId = parseStringInLong(film);
        long userId = parseStringInLong(user);

        if (!filmStorage.allFilms().get(filmId).getLikes().contains(userId)) {
            log.error("The user did not like this movie");
            throw new BusinessLogicException("The user did not like this movie");
        }

        log.info("User №" + userId + " removed the varnish from the film №" + filmId);

        return filmStorage.delLikeFilm(filmId,userId);
    }

    public Long parseStringInLong(String str) {

        long a = 0;

        try {
            a = Long.parseLong(str);
        } catch (NumberFormatException e) {
            log.error("\"" + str + "\" must be a number");
            throw new ValidationException("\"" + str + "\" must be a number");
        }

        if (a <= 0) {
            log.error("\"" + str + "\" must be positive");
            throw new FilmNotFoundException("\"" + str + "\" must be positive");
        }

        return a;
    }

}
