package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;


@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

//    private static final LocalDate BEGIN_TIME = LocalDate.of(1895,  Month.DECEMBER,28);
//
//    public final Map<Integer, Film> films = new HashMap<>();
//
//
//    private static int newId = 1;
//    private static int getNewId() {
//        return newId++;
//    }
//    public static void setNewId(int newId) {
//        FilmController.newId = newId;
//    }

    // получение всех фильмов.
    @GetMapping
    public Collection<Film> findAll() {
//        return films.values();
        return inMemoryFilmStorage.findAll();
    }

    // добавление фильма.
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

//        if (film.getName() == null || film.getName().isBlank()) {
//            log.error("Name of the film cannot be empty");
//            throw new MyValidationException("Name of the film cannot be empty");
//
//        } else if (film.getDescription() != null && film.getDescription().length() > 200) {
//            log.error("The maximum length of the description should not exceed 200 characters");
//            throw new MyValidationException("The maximum length of the description should not exceed 200 characters");
//
//        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(BEGIN_TIME)) {
//            log.error("The release date may not be earlier than December 28, 1895");
//            throw new MyValidationException("The release date may not be earlier than December 28, 1895");
//
//        } else if (film.getDuration() != null && film.getDuration() <= 0) {
//            log.error("The duration of the film should be positive");
//            throw new MyValidationException("The duration of the film should be positive");
//
//        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(BEGIN_TIME)) {
//            log.error("The release date may not be earlier than December 28, 1895");
//            throw new MyValidationException("The release date may not be earlier than December 28, 1895");
//        }
//
//        film = new Film(getNewId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
//
//        films.put(film.getId(), film);
//        log.info("Added Film: {}", film.getName());
//        return film;
        return inMemoryFilmStorage.create(film);
    }

    // обновление фильма.
    @PutMapping
    public Film put(@RequestBody Film film) {

//        if (films.containsKey(film.getId())) {
//            films.put(film.getId(), film);
//        } else {
//            log.error("There is no such film in our list of films");
//            throw new MyValidationException("There is no such film in our list of films");
//        }
//
//        log.info("Updated movie description: {}", film.getName());
//        return film;
        return inMemoryFilmStorage.put(film);
    }

}