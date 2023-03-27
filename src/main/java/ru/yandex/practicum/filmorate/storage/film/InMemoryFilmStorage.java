package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MyValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate BEGIN_TIME = LocalDate.of(1895,  Month.DECEMBER,28);

    public final Map<Integer, Film> films = new HashMap<>();

    private static int newId = 1;
    private static int getNewId() {
        return newId++;
    }
    public static void setNewId(int newId) {
        InMemoryFilmStorage.newId = newId;
    }



    @Override
    public Collection<Film> findAll() {
        return films.values();
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

        films.put(film.getId(), film);
        log.info("Added Film: {}", film.getName());
        return film;
    }

    @Override
    public Film put(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.error("There is no such film in our list of films");
            throw new MyValidationException("There is no such film in our list of films");
        }

        log.info("Updated movie description: {}", film.getName());
        return film;
    }

    @Override
    public Film del(Film film) {
        return null;
    }
}
