package ru.yandex.practicum.filmorate.service.film.util;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Comparator;

public class LikesComparator implements Comparator<Film> {

    @Override
    public int compare(Film o1, Film o2) {
        return o1.getLikes().size() - o2.getLikes().size();
    }
}
