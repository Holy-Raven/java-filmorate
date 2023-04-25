package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
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

    @GetMapping("/{id}")
    public Optional<Film> findFilmById(@PathVariable("id") String id) {
        return filmService.findFilmById(id);
    }

    //  пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") String id,
                        @PathVariable("userId") String userId) {
        return filmService.addLikeFilm(id, userId);
    }

    //  пользователь удаляет лайк
    @DeleteMapping("/{id}/like/{userId}")
    public Film delLike(@PathVariable("id") String id,
                        @PathVariable("userId") String userId) {
        return filmService.delLikeFilm(id, userId);
    }

    // возвращает список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public List<Film> popularFilmList(@RequestParam(defaultValue = "10") String count) {
        return filmService.sortFilm(count);
    }

}