package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Date;
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

        String name = film.getName();
        String description = film.getDescription();
        Date releasedate = java.sql.Date.valueOf(film.getReleaseDate());
        Integer duration = film.getDuration();
        Long mpa_id = film.getMpa_id();

        this.jdbcTemplate.update(
                "INSERT INTO FilMS AS F (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?" +
                        name, description, releasedate, duration, mpa_id );
        return film;
    }

    @Override
    public Film put(Film film) {
        return null;
    }

    @Override
    public Film del(Film film) {
        return null;
    }

    @Override
    public Film findFilmById(Long filmId) {
        // выполняем запрос к базе данных.
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS AS F where F.FILM_ID = ?", filmId);

        // обрабатываем результат выполнения запроса
        if(filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
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
