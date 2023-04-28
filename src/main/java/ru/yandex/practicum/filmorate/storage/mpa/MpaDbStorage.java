package ru.yandex.practicum.filmorate.storage.mpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Constant.MPA_MAPPER;


@Repository("MpaStorage")
public class MpaDbStorage implements MpaStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> findById(long id) {

        String selectSql = "SELECT * FROM MPA WHERE MPA_ID = ?";

        try {

            Mpa mpa = jdbcTemplate.queryForObject(selectSql, MPA_MAPPER, id);

            log.info("Найден mpa: {} {}" , mpa.getId(), mpa.getName());
            return Optional.of(mpa);

        } catch (EmptyResultDataAccessException exception) {

            log.info("mpa с идентификатором {} не найден.", id);
            throw new MpaNotFoundException("Mpa с id " +  id + " не найден");
        }


    }
    @Override
    public List<Mpa> findAll() {
        String selectSql = "SELECT * FROM MPA ORDER BY MPA_ID DESC";
        return jdbcTemplate.query(selectSql, MPA_MAPPER);
    }
}

