package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.film.FilmServiceInterface;

import javax.validation.Valid;


@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmServiceInterface filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // получение всех фильмов.
    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    // добавление фильма.
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    // обновление фильма.
    @PutMapping
    public Film put(@RequestBody Film film) {
        return filmService.update(film);
    }

    // удаление фильма
    @DeleteMapping
    public Film del(@RequestBody Film film) {
        return filmService.delete(film);
    }

    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable("filmId") String filmId){
        return filmService.findFilmById(filmId);
    }

}