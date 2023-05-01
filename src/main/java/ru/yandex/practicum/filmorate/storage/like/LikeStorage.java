package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {

    void addLikeFilm(Long film, Long user);

    void delLikeFilm(Long film, Long user);

    List<Long> findLikesListFilmById(long film);

    boolean isExist(long filmId, long userId);
}
