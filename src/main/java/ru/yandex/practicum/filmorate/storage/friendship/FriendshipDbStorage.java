package ru.yandex.practicum.filmorate.storage.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Constant.FRIENDSHIP_MAPPER;

@Repository("FriendshipDbStorage")
public class FriendshipDbStorage implements FriendshipStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List<Long> findAllById(long id) {

        String sql = "SELECT SECOND_USER_ID FROM FRIENDSHIP WHERE FIRST_USER_ID = ? " +
                     "UNION SELECT FIRST_USER_ID FROM FRIENDSHIP WHERE SECOND_USER_ID = ? AND STATUS = TRUE";

        List<Long> friendList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("SECOND_USER_ID"), id, id);
        log.info("Список друзей пользователя: {}" , id);

        return friendList;
    }
    @Override
    public void add(Friendship friendship) {

        String sql = "INSERT INTO FRiENDSHIP (FIRST_USER_ID,  SECOND_USER_ID) VALUES (?, ?)";

        jdbcTemplate.update(sql, friendship.getFirst_user_id(), friendship.getSecond_user_id());

        log.info("Отправлен запрос на дружбу от пользователя {} к {}" , friendship.getFirst_user_id(), friendship.getSecond_user_id());

    }

    @Override
    public void put(Friendship friendship) {

        String sql = "UPDATE FRiENDSHIP SET STATUS = ? WHERE FIRST_USER_ID = ? and SECOND_USER_ID = ? OR FIRST_USER_ID = ? AND SECOND_USER_ID = ?";

        jdbcTemplate.update(sql, true, friendship.getFirst_user_id(), friendship.getSecond_user_id(),
                friendship.getSecond_user_id(), friendship.getFirst_user_id());

        log.info("Обновлен статус дружбы на подтвержденный у пользователей: {} {}" , friendship.getFirst_user_id(), friendship.getSecond_user_id());

    }

    @Override
    public Optional<Friendship> findFriendship(Friendship friendship) {

        String sql = "SELECT * FROM FRIENDSHIP WHERE FIRST_USER_ID = ? AND SECOND_USER_ID = ? " +
                     "OR SECOND_USER_ID = ? AND FIRST_USER_ID = ?";
        try {

            friendship = jdbcTemplate.queryForObject(sql, FRIENDSHIP_MAPPER, friendship.getFirst_user_id(),
                     friendship.getSecond_user_id(), friendship.getSecond_user_id(), friendship.getFirst_user_id());

            log.info("Запись найдена: {} {}" , friendship.getFirst_user_id(), friendship.getSecond_user_id());
            return Optional.of(friendship);

        } catch (EmptyResultDataAccessException exception) {

            log.info("Запись не найдена: {} {}" , friendship.getFirst_user_id(), friendship.getSecond_user_id());
            return Optional.empty();
        }
    }

    @Override
    public void del(Friendship friendship) {
        String sql = "DELETE FROM FRIENDSHIP WHERE FIRST_USER_ID = ? AND SECOND_USER_ID = ? " +
                     "OR FIRST_USER_ID = ? AND SECOND_USER_ID = ?";

        jdbcTemplate.update(sql, friendship.getFirst_user_id(),
                friendship.getSecond_user_id(), friendship.getSecond_user_id(), friendship.getFirst_user_id());

        log.info("Больше не друзья пользователи: {} {}" , friendship.getFirst_user_id(), friendship.getSecond_user_id());

    }

    @Override
    public boolean status(Friendship friendship) {

        String sql = "SELECT STATUS FROM FRIENDSHIP WHERE FIRST_USER_ID = ? AND SECOND_USER_ID = ? " +
                     "OR FIRST_USER_ID = ? AND SECOND_USER_ID = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, friendship.getFirst_user_id(), friendship.getSecond_user_id(),
                friendship.getSecond_user_id(), friendship.getFirst_user_id());

        if (userRows.next()) {

            if (userRows.getBoolean("status")) {
                log.info("Пользователи друзья: {} {}", friendship.getFirst_user_id(), friendship.getSecond_user_id());
                return true;
            } else {
                log.info("Пользователи не друзья: {} {}", friendship.getFirst_user_id(), friendship.getSecond_user_id());
                return false;
            }

        } else {
            log.info("Данные о дружбе пользователей {} и {} не найдены", friendship.getFirst_user_id(), friendship.getSecond_user_id());
            return false;
        }
    }

    @Override
    public boolean isExist(Friendship friendship) {
        return findFriendship(friendship).isPresent();
    }

}
