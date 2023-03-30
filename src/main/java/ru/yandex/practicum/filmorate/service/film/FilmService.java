package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService implements FilmServiceInterface {

    FilmStorage filmStorage;

    private static final LocalDate BEGIN_TIME = LocalDate.of(1895,  Month.DECEMBER,28);

    private static long newId = 1;
    private static long getNewId() {
        return newId++;
    }
    public static void setNewId(int newId) {
        FilmService.newId = newId;
    }

    @Autowired
    FilmService(InMemoryFilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.allFilms().values();
    }

    @Override
    public Film create(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Name of the film cannot be empty");
            throw new ValidationException("Name of the film cannot be empty");

        } else if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("The maximum length of the description should not exceed 200 characters");
            throw new ValidationException("The maximum length of the description should not exceed 200 characters");

        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(BEGIN_TIME)) {
            log.error("The release date may not be earlier than December 28, 1895");
            throw new ValidationException("The release date may not be earlier than December 28, 1895");

        } else if (film.getDuration() != null && film.getDuration() <= 0) {
            log.error("The duration of the film should be positive");
            throw new ValidationException("The duration of the film should be positive");

        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(BEGIN_TIME)) {
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
            throw new NotFoundException("There is no such film in our list of films");
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
            throw new NotFoundException("There is no such film in our list of films");
        }

        log.info("Movie deleted {}", film.getName());
        return film;

    }

    @Override
    public Film findFilmById(String filmId) {

        long id = parseStringInLong(filmId);

        if (filmStorage.allFilms().containsKey(id)){
            return filmStorage.allFilms().get(id);
        } else {
            throw new NotFoundException("There is no such film in our list of films");
        }
    }

    @Override
    public List<Film> sortFilm(String count) {

        long size = parseStringInLong(count);

        log.info("Cписок фильмов отсортированный по их популярности");
        return filmStorage.allFilms().values()
                                     .stream()
                                     .sorted((film1, film2) -> film2.getLikes().size()
                                                             - film1.getLikes().size())
                                     .limit(size)
                                     .collect(Collectors.toList());
    }

    @Override
    public Film addLikeFilm(String film, String user) {

        long filmId = parseStringInLong(film);
        long userId = parseStringInLong(user);

        if (filmStorage.allFilms().get(filmId).getLikes().contains(userId)) {
            log.error("Пользователь уже поставил лайк этому фильму");
            throw new RuntimeException("Пользователь уже поставил лайк этому фильму");
        }

        findFilmById(film).getLikes().add(userId);
        log.info("User №" + userId + " поставил лайк фильму №" + filmId);

        return filmStorage.allFilms().get(filmId);
    }

    @Override
    public Film delLikeFilm(String film, String user) {

        long filmId = parseStringInLong(film);
        long userId = parseStringInLong(user);

        if (!filmStorage.allFilms().get(filmId).getLikes().contains(userId)) {
            log.error("Пользователь не ставил лайк этому фильму");
            throw new RuntimeException("Пользователь не ставил лайк этому фильму");
        }

        findFilmById(film).getLikes().remove(userId);
        log.info("User №" + userId + " удалили лайк с фильма №" + filmId);

        return filmStorage.allFilms().get(filmId);
    }

    public Long parseStringInLong (String str){

        long a;

        try {
            a = Long.parseLong(str);
        } catch (NumberFormatException e){
            log.error("\"" + str + "\" must be a number");
            throw new ValidationException( "\"" + str + "\" must be a number");
        }

        if (a <= 0){
            log.error("\"" + str + "\" must be positive");
            throw new NotFoundException("\"" + str + "\" must be positive");
        }

        return a;
    }

}
