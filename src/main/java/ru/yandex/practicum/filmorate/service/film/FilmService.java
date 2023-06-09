package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BusinessLogicException;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService implements FilmServiceInterface {

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;

    private final MpaStorage mpaStorage;

    private final GenreStorage genreStorage;

    private final LikeStorage likeStorage;

    @Override
    public List<Film> findAll() {

        List<Film> films = new ArrayList<>();

        for (Film film : filmStorage.allFilms()) {

            Film newFilm = new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    mpaStorage.findById(film.getMpa().getId()).get());

            newFilm.getGenres().addAll(filmStorage.findGenreListFilmById(newFilm.getId()));
            newFilm.getLikes().addAll(likeStorage.findLikesListFilmById(film.getId()));

            films.add(newFilm);
        }

        log.info("All Films");
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

        Film newFilm = updateGenre(film);

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

            newFilm.getLikes().addAll(likeStorage.findLikesListFilmById(film.getId()));

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
            film.getLikes().addAll(likeStorage.findLikesListFilmById(film.getId()));


            log.info("Film id {}", film.getId());
            return Optional.of(film);
        } else {
            throw new FilmNotFoundException("There is no such film in our list of films");
        }
    }

    @Override
    public List<Film> sortFilm(String count) {

        long size = parseStringInLong(count);

        log.info("list of the {} most popular films", count);
        return findAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size()
                                        - film1.getLikes().size())
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLikeFilm(String strFilmId, String strUserId) {

        long filmId = parseStringInLong(strFilmId);
        long userId = parseStringInLong(strUserId);

        Film film = filmStorage.findFilmById(filmId).get();

        if (likeStorage.isExist(filmId, userId)) {

            log.error("The user has already liked this movie");
            throw new BusinessLogicException("The user has already liked this movie");

        } else {

            likeStorage.addLikeFilm(filmId, userId);

            film = new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    mpaStorage.findById(film.getMpa().getId()).get());

            film.getGenres().addAll(filmStorage.findGenreListFilmById(film.getId()));
            film.getLikes().addAll(likeStorage.findLikesListFilmById(film.getId()));

        }

        log.info("User № {} liked the film № {}", userId, filmId);
        return film;
    }

    @Override
    public Film delLikeFilm(String strFilmId, String strUserId) {


        long filmId = parseStringInLong(strFilmId);
        long userId = parseStringInLong(strUserId);

        Film film = filmStorage.findFilmById(filmId).get();

        if (!likeStorage.isExist(filmId, userId)) {

            log.error("The user did not like this movie");
            throw new BusinessLogicException("The user did not like this movie");

        } else {
            log.info("User id {} deleted a like from Film id {}", userId, filmId);
            likeStorage.delLikeFilm(filmId, userId);
        }

        return film;
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
