package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Mappers.GENRE_MAPPER;

@RequiredArgsConstructor
@Repository("GenreStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> findById(long id) {

        String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, GENRE_MAPPER, id));
        } catch (EmptyResultDataAccessException exception) {
            throw new GenreNotFoundException("Genre с id " +  id + " не найден");
        }
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM GENRE ORDER BY GENRE_ID";
        return jdbcTemplate.query(sql, GENRE_MAPPER);
    }
}
