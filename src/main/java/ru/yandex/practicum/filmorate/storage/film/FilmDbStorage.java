package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Mappers.FILM_MAPPER;
import static ru.yandex.practicum.filmorate.util.Mappers.GENRE_MAPPER;
@Slf4j
@RequiredArgsConstructor
@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> allFilms() {

        String sql = "SELECT * FROM FILMS";

        try {
            return jdbcTemplate.query(sql, FILM_MAPPER);
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Film add(Film film) {

        String sql = "INSERT INTO FilMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;

        }, keyHolder);

        Film newFilm = new Film(keyHolder.getKey().longValue(), film.getName(), film.getDescription(),
                 film.getReleaseDate(), film.getDuration(), film.getMpa());

        newFilm.getGenres().addAll(film.getGenres());
        for (Genre genre : newFilm.getGenres()) {
            addGenreToFilm(newFilm.getId(), genre.getId());
        }

         return newFilm;

    }

    @Override
    public void put(Film film) {

        String sql = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";

        this.jdbcTemplate.update(sql, film.getName(), film.getDescription(), java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),film.getMpa().getId(), film.getId());
    }

    @Override
    public void del(Film film) {

        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";

        this.jdbcTemplate.update(sql,film.getId());
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {

        String sql = "SELECT * FROM FILMS WHERE FILM_ID = ?";

        try {

            Film film = jdbcTemplate.queryForObject(sql, FILM_MAPPER, filmId);
            return Optional.of(film);

        } catch (EmptyResultDataAccessException exception) {
            log.info("Film id{} not found.", filmId);
            return Optional.empty();
        }
    }

    @Override
    public void addGenreToFilm(long film, long genre) {

            String sqlInsert = "INSERT INTO FILMS_GENRE(FILM_ID, GENRE_ID) VALUES (?, ?)";

            jdbcTemplate.update(sqlInsert, film, genre);
    }

    @Override
    public void delGenresListFromFilm(long film) {

        String sql = "DELETE FROM FILMS_GENRE WHERE FILM_ID = ?";

        jdbcTemplate.update(sql, film);

    }

    @Override
    public List<Genre> findGenreListFilmById(long film) {

        String sql = "SELECT g.* FROM FILMS_GENRE AS fg " +
                     "JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID " +
                     "WHERE fg.FILM_ID =? " +
                     "ORDER BY g.GENRE_ID";

        List<Genre> genreList = jdbcTemplate.query(sql, GENRE_MAPPER, film);

        return genreList;

    }

    public boolean existsById(Long filmId) {
        return findFilmById(filmId).isPresent();
    }

}
