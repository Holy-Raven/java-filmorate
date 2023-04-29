package ru.yandex.practicum.filmorate.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Constant.GENRE_MAPPER;
@Repository("GenreStorage")
public class GenreDbStorage implements GenreStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> findById(long id) {

        String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";

        try {

            Genre genre = jdbcTemplate.queryForObject(sql, GENRE_MAPPER, id);

            log.info("Найден genre: {} {}" , genre.getId(), genre.getName());
            return Optional.of(genre);

        } catch (EmptyResultDataAccessException exception) {

            log.info("genre с идентификатором {} не найден.", id);
            throw new GenreNotFoundException("Genre с id " +  id + " не найден");
        }

    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM GENRE ORDER BY GENRE_ID";
        return jdbcTemplate.query(sql, GENRE_MAPPER);
    }
}
