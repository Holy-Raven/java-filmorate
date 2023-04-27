package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Constant.FILM_MAPPER;

@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List <Film> allFilms() {

        String sql = "SELECT * FROM FILMS";

        try {
            log.info("Список пользователей");
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

         return new Film(keyHolder.getKey().longValue(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa());

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
    public Optional <Film> findFilmById(Long filmId) {

        String sql = "SELECT * FROM FILMS WHERE FILM_ID = ?";

        Film film = jdbcTemplate.queryForObject(sql, FILM_MAPPER, filmId);

        if (film != null) {
            log.info("Найден фильм: {} {}" , film.getId(), film.getName());
            return Optional.of(film);
        } else {
           log.info("Фильм с идентификатором {} не найден.", filmId);
            return Optional.empty();
        }


//        // выполняем запрос к базе данных.
//        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", filmId);
//
//        // обрабатываем результат выполнения запроса
//        if(filmRows.next()) {
//            Film film = new Film(
//                    filmId,
//                    filmRows.getString("name"),
//                    filmRows.getString("description"),
//                    LocalDate.ofInstant(filmRows.getDate("releasedate").toInstant(), ZoneId.systemDefault()),
//                    filmRows.getInt("duration"),
//                    new Mpa(filmRows.getLong("mpa_id")));
//
//            log.info("Найден фильм: {} {}" , film.getId(), film.getName());
//            return Optional.of(film);
//        } else {
//            log.info("Фильм с идентификатором {} не найден.", filmId);
//            return Optional.empty();
//        }
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


    public boolean existsById(Long filmId) {
        return findFilmById(filmId).isPresent();
    }

}
