package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, Film> allFilms() {
        return null;
    }

    @Override
    public Film add(Film film) {

        String sqlQuery = "INSERT INTO FilMS AS F (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

         jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa_id());
            return stmt;

        }, keyHolder);
//
//        SqlParameterSource filmParams = new MapSqlParameterSource()
//                .addValues("film_id",film.film )
//
//
//
//        int num = namedParameterJdbcTemplate.update(sqlQuery, filmParams, keyHolder)

        return new Film(keyHolder.getKey().longValue(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),null);
    }

    @Override
    public Film put(Film film) {

        this.jdbcTemplate.update(
                "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, MPA_ID = ? where FILM_ID = ?",
                film.getName(), film.getDescription(), java.sql.Date.valueOf(film.getReleaseDate()), film.getDuration(),film.getMpa_id(), film.getId());

        return film;
    }

    @Override
    public Film del(Film film) {

        this.jdbcTemplate.update(
                "delete from FILMS where FILM_ID = ?",
                Long.valueOf(film.getId()));

        return film;
    }

    @Override
    public Film findFilmById(Long filmId) {
        // выполняем запрос к базе данных.
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS AS F where F.FILM_ID = ?", filmId);

        // обрабатываем результат выполнения запроса
        if(filmRows.next()) {
            Film film = new Film(
                    filmId,
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    LocalDate.ofInstant(filmRows.getDate("releasedate").toInstant(), ZoneId.systemDefault()),
                    filmRows.getInt("duration"),
                    filmRows.getLong("mpa_id"));

            log.info("Найден фильм: {} {}" , film.getId(), film.getName());

            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            return null;
        }
    }

    @Override
    public Film addLikeFilm(Long film, Long user) {
        return null;
    }

    @Override
    public Film delLikeFilm(Long film, Long user) {
        return null;
    }

    @Override
    public List<Film> sortFilm(Long size) {
        return null;
    }
}
