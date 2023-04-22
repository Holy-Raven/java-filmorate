package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;



import org.springframework.jdbc.core.JdbcTemplate;


import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final Logger log = (Logger) LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, User> allUsers() {
        return null;
    }

    @Override
    public User add(User user) {

        String sql = "INSERT INTO users (email, name, login, birthday) VALUES (?, ?, ?, ?)";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("INSERT INTO users (email, name, login, birthday) VALUES (?, ?, ?, ?, EMAIL,NAME,LOGIN,BIRTHDAY )");

        return user;
    }

    @Override
    public User put(User user) {
        return null;
    }

    @Override
    public User del(User user) {
        return null;
    }

    @Override
    public User findUserById(Long userId) {
        return null;
    }

    @Override
    public User addFriends(Long user1, Long user2) {
        return null;
    }

    @Override
    public User delFriends(Long user1, Long user2) {
        return null;
    }

    @Override
    public List<User> friendsList(Long user) {
        return null;
    }
}
