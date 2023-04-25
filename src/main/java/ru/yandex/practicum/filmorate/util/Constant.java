package ru.yandex.practicum.filmorate.util;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Month;

public class Constant {

    public static final LocalDate BEGIN_TIME = LocalDate.of(1895,  Month.DECEMBER,28);
    public static final RowMapper<User> USER_MAPPER = (ResultSet rs, int rowNum) -> {

        User user = new User(rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("login"),
                rs.getDate("birthday").toLocalDate());
        return user;
    };

    public static final RowMapper<Film> FILM_MAPPER = (ResultSet rs, int rowNum) -> {

        Film film = new Film(rs.getLong("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                rs.getLong("mpa_id"));
        return film;
    };

    public static final RowMapper<Mpa> MPA_MAPPER = (ResultSet rs, int rowNum) -> {

        Mpa mpa = new Mpa(rs.getLong("mpa_id"),
                rs.getString("name"),
                rs.getString("description"));

        return mpa;
    };

    public static final RowMapper<Genre> GENRE_MAPPER = (ResultSet rs, int rowNum) -> {

        Genre genre = new Genre(rs.getLong("genre_id"),
                rs.getString("name"));
        return genre;
    };

}