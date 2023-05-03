package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreServiceInterface {

    List<Genre> findAll();

    Genre findById(long id);
}
