package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.util.Constant;

import java.util.*;

@Service
@Slf4j
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;


    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, MpaStorage mpaStorage1, GenreStorage genreStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage1;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }

    @Override
    public List<Film> findAll() {

        List<Film> films = new ArrayList<>();

        for (Film film : filmStorage.allFilms()) {

            Film newFilm = new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    mpaStorage.findById(film.getMpa().getId()).get());

            newFilm.getGenres().addAll(filmStorage.findGenreListFilmById(newFilm.getId()));


            films.add(newFilm);
        }

        return films;
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

        Film newFilm = new Film(null, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                mpaStorage.findById(film.getMpa().getId()).get());

        newFilm.getGenres().addAll(film.getGenres());
        newFilm = updateGenre(film);

        log.info("Added Film: {}", film.getName());
        return filmStorage.add(newFilm);
    }

    @Override
    public Film update(Film film) {

        if (filmStorage.existsById(film.getId())) {

            filmStorage.put(film);

            Film newFilm = updateGenre(film);

            filmStorage.delGenresListFromFilm(newFilm.getId());
            for (Genre genre : newFilm.getGenres()) {
                filmStorage.addGenreToFilm(newFilm.getId(), genre.getId());
            }



            log.info("Updated movie description: {}", film.getName());
            return newFilm;
        } else {
            log.error("There is no such film in our list of films");
            throw new FilmNotFoundException("There is no such film in our list of films");
        }

    }

    @Override
    public Film delete(Film film) {

        if (filmStorage.existsById(film.getId())) {

            filmStorage.delGenresListFromFilm(film.getId());
            filmStorage.del(film);

        } else {
            log.error("There is no such film in our list of films");
            throw new FilmNotFoundException("There is no such film in our list of films");
        }

        log.info("Movie deleted {}", film.getName());
        return film;

    }

    @Override
    public Optional<Film> findFilmById(String filmId) {

        long id = parseStringInLong(filmId);

        if (filmStorage.existsById(id)) {

            Film film = filmStorage.findFilmById(id).get();

            film = new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    mpaStorage.findById(film.getMpa().getId()).get());

            film.getGenres().addAll(filmStorage.findGenreListFilmById(id));


            return Optional.of(film);
        } else {
            throw new FilmNotFoundException("There is no such film in our list of films");
        }
    }

    @Override
    public List<Film> sortFilm(String count) {
//
//        long size = parseStringInLong(count);
//
//        log.info("Cписок фильмов отсортированный по их популярности");
//        return filmStorage.sortFilm(size);
        return null;
    }

    @Override
    public Film addLikeFilm(String film, String user) {

//        long filmId = parseStringInLong(film);
//        long userId = parseStringInLong(user);
//
//        if (filmStorage.allFilms().get(filmId).getLikes().contains(userId)) {
//            log.error("The user has already liked this movie");
//            throw new BusinessLogicException("The user has already liked this movie");
//        }
//
//        log.info("User №" + userId + " I liked the movie №" + filmId);
//
//        return filmStorage.addLikeFilm(filmId,userId);

        return null;
    }

    @Override
    public Film delLikeFilm(String film, String user) {

//        long filmId = parseStringInLong(film);
//        long userId = parseStringInLong(user);
//
//        if (!filmStorage.allFilms().get(filmId).getLikes().contains(userId)) {
//            log.error("The user did not like this movie");
//            throw new BusinessLogicException("The user did not like this movie");
//        }
//
//        log.info("User №" + userId + " removed the varnish from the film №" + filmId);
//
//        return filmStorage.delLikeFilm(filmId,userId);

        return null;
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

    public Film updateGenre(Film film) {

        Film newFilm = new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                mpaStorage.findById(film.getMpa().getId()).get());

        newFilm.getGenres().addAll(film.getGenres());

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {

                if (film.getGenres().contains(genreStorage.findById(genre.getId()).get())) {
                    newFilm.getGenres().remove(genreStorage.findById(genre.getId()).get());
                    newFilm.getGenres().add(genreStorage.findById(genre.getId()).get());
                } else {
                    newFilm.getGenres().add(genreStorage.findById(genre.getId()).get());
                }
            }
        }
        return newFilm;
    }
}
