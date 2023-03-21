
package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.MyValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;


@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private static final LocalDate BEGIN_TIME = LocalDate.of(1895,  Month.DECEMBER,28);
    public final Map<Integer, Film> films = new HashMap<>();


    private static int newId = 1;
    private static int getNewId() {
        return newId++;
    }
    public static void setNewId(int newId) {
        FilmController.newId = newId;
    }

    // получение всех фильмов.
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    // добавление фильма.
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        //раньше было так (сняла комментарии, что бы тесты написать)
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым");
            throw new MyValidationException("Название фильма не может быть пустым");

        } else if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Максимальная длина описания не должна превышать 200 символов");
            throw new MyValidationException("Максимальная длина описания не должна превышать 200 символов");

        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(BEGIN_TIME)) {
            log.error("Дата релиза может быть не раньше 28 декабря 1895 года");
            throw new MyValidationException("Дата релиза может быть не раньше 28 декабря 1895 года");

        } else if (film.getDuration() != null && film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new MyValidationException("Продолжительность фильма должна быть положительной");

        }
        // без всего того что выше в postman и так работает


        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(BEGIN_TIME)) {
            log.error("Дата релиза может быть не раньше 28 декабря 1895 года");
            throw new MyValidationException("Дата релиза может быть не раньше 28 декабря 1895 года");
        }

        film = new Film(getNewId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());

        films.put(film.getId(), film);
        log.info("Добавлен Фильм: {}", film.getName());
        return film;
    }

    // обновление фильма.
    @PutMapping
    public Film put(@RequestBody Film film) {

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.error("Такого фильма нет в нашем списке фильмов");
            throw new MyValidationException("Такого фильма нет в нашем списке фильмов");
        }

        log.info("Обновлено описание фильма: {}", film.getName());
        return film;
    }

}