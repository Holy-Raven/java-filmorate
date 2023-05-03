package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

import static ru.yandex.practicum.filmorate.util.Constant.LIKE_MAPPER;

@Repository("LikeStorage")
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLikeFilm(Long film, Long user) {

        String sql = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES (?, ?)";

        jdbcTemplate.update(sql, film, user);

    }

    @Override
    public void delLikeFilm(Long film, Long user) {

        String sql = "DELETE FROM LIKES WHERE (FILM_ID = ? AND USER_ID = ?)";

        jdbcTemplate.update(sql, film, user);

    }

    @Override
    public List<Long> findLikesListFilmById(long film) {

        String sql = "SELECT USER_ID FROM LIKES WHERE FILM_ID =?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), film);

    }

    @Override
    public boolean isExist(long filmId, long userId) {

        String sql = "SELECT * FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";

        try {
            jdbcTemplate.queryForObject(sql, LIKE_MAPPER, filmId, userId);
            return true;

        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }
}
