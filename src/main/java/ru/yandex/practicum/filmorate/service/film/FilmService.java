package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.util.LikesComparator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;


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
            throw new MyValidationException("Name of the film cannot be empty");

        } else if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("The maximum length of the description should not exceed 200 characters");
            throw new MyValidationException("The maximum length of the description should not exceed 200 characters");

        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(BEGIN_TIME)) {
            log.error("The release date may not be earlier than December 28, 1895");
            throw new MyValidationException("The release date may not be earlier than December 28, 1895");

        } else if (film.getDuration() != null && film.getDuration() <= 0) {
            log.error("The duration of the film should be positive");
            throw new MyValidationException("The duration of the film should be positive");

        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(BEGIN_TIME)) {
            log.error("The release date may not be earlier than December 28, 1895");
            throw new MyValidationException("The release date may not be earlier than December 28, 1895");
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
            throw new MyValidationException("There is no such film in our list of films");
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
            throw new MyValidationException("There is no such film in our list of films");
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
            throw new MyValidationException("There is no such film in our list of films");
        }
    }

    @Override
    public List<Film> sortFilm(String count) {

        long a;

        if (count == null || count.isBlank() ) {
            a = 10;
        } else {
            a = parseStringInLong(count);
        }

        List<Film> filmList = new ArrayList<>(filmStorage.allFilms().values());
        Comparator<Film> likesComparator = new LikesComparator();
        filmList.sort(likesComparator);

        List<Film> newSortFilms = new ArrayList<>();

        while (a >= 0) {

            for (Film film : filmList) {
                newSortFilms.add(film);
                a--;
            }
        }

    return newSortFilms;

    }

    @Override
    public Film addLikeFilm(String film, String user) {

        long filmId = parseStringInLong(film);
        long userId = parseStringInLong(user);

        if (filmStorage.allFilms().get(filmId).getLikes().contains(userId)) {
            log.error("Пользователь уже поставил лайк этому фильму");
            throw new MyValidationException("Пользователь уже поставил лайк этому фильму");
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
            throw new MyValidationException("Пользователь не ставил лайк этому фильму");
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
            throw new MyValidationException("\"" + str + "\" must be a number");
        }

        if (a <= 0){
            log.error("\"" + str + "\" must be positive");
            throw new MyValidationException("\"" + str + "\" must be positive");
        }

        return a;
    }

}
