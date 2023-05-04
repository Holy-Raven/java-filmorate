package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Mappers.MPA_MAPPER;

@Slf4j
@RequiredArgsConstructor
@Repository("MpaStorage")
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> findById(long id) {

        String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";

        try {

            Mpa mpa = jdbcTemplate.queryForObject(sql, MPA_MAPPER, id);

            log.info("Find mpa {} name {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);

        } catch (EmptyResultDataAccessException exception) {

            log.info("mpa {} not found.", id);
            throw new MpaNotFoundException("Mpa " +  id + " not found");
        }
    }

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * FROM MPA ORDER BY MPA_ID";
        return jdbcTemplate.query(sql, MPA_MAPPER);
    }
}

