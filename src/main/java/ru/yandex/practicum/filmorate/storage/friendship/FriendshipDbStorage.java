package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Mappers.FRIENDSHIP_MAPPER;

@Slf4j
@RequiredArgsConstructor
@Repository("FriendshipDbStorage")
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> findAllById(long id) {

        String sql = "SELECT SECOND_USER_ID FROM FRIENDSHIP WHERE FIRST_USER_ID = ? " +
                     "UNION SELECT FIRST_USER_ID FROM FRIENDSHIP WHERE SECOND_USER_ID = ? AND STATUS = TRUE";

        List<Long> friendList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("SECOND_USER_ID"), id, id);

        return friendList;
    }

    @Override
    public void add(Friendship friendship) {

        String sql = "INSERT INTO FRiENDSHIP (FIRST_USER_ID,  SECOND_USER_ID) VALUES (?, ?)";

        jdbcTemplate.update(sql, friendship.getFirstUserId(), friendship.getSecondUserId());

    }

    @Override
    public void put(Friendship friendship) {

        String sql = "UPDATE FRiENDSHIP SET STATUS = ? WHERE FIRST_USER_ID = ? and SECOND_USER_ID = ? OR FIRST_USER_ID = ? AND SECOND_USER_ID = ?";

        jdbcTemplate.update(sql, true, friendship.getFirstUserId(), friendship.getSecondUserId(),
                friendship.getSecondUserId(), friendship.getFirstUserId());

    }

    @Override
    public Optional<Friendship> findFriendship(Friendship friendship) {

        String sql = "SELECT * FROM FRIENDSHIP WHERE FIRST_USER_ID = ? AND SECOND_USER_ID = ? " +
                     "OR SECOND_USER_ID = ? AND FIRST_USER_ID = ?";
        try {

            friendship = jdbcTemplate.queryForObject(sql, FRIENDSHIP_MAPPER, friendship.getFirstUserId(),
                     friendship.getSecondUserId(), friendship.getSecondUserId(), friendship.getFirstUserId());

            return Optional.of(friendship);

        } catch (EmptyResultDataAccessException exception) {

            log.info("User id{} and User id {} nor friends", friendship.getFirstUserId(), friendship.getSecondUserId());
            return Optional.empty();
        }
    }

    @Override
    public void del(Friendship friendship) {
        String sql = "DELETE FROM FRIENDSHIP WHERE FIRST_USER_ID = ? AND SECOND_USER_ID = ? " +
                     "OR FIRST_USER_ID = ? AND SECOND_USER_ID = ?";

        jdbcTemplate.update(sql, friendship.getFirstUserId(),
                friendship.getSecondUserId(), friendship.getSecondUserId(), friendship.getFirstUserId());
    }

    @Override
    public boolean status(Friendship friendship) {

        String sql = "SELECT STATUS FROM FRIENDSHIP WHERE FIRST_USER_ID = ? AND SECOND_USER_ID = ? " +
                     "OR FIRST_USER_ID = ? AND SECOND_USER_ID = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, friendship.getFirstUserId(), friendship.getSecondUserId(),
                friendship.getSecondUserId(), friendship.getFirstUserId());

        if (userRows.next()) {
            return userRows.getBoolean("status");
        } else {
            return false;
        }
    }

    @Override
    public boolean isExist(Friendship friendship) {
        return findFriendship(friendship).isPresent();
    }

}
