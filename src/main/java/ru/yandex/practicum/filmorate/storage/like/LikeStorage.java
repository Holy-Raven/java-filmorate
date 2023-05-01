package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {


    void delLikeFromFilm(Long film, Long user);

    void addLikeToFilm(Long film, Long user);

    List<Long> findLikesListFilmById(long film);

}
