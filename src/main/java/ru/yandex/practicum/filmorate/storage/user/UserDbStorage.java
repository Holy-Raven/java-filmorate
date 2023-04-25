package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Constant.USER_MAPPER;

@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> allUsers() {

        String selectSql = "SELECT * FROM USERS";

        try {
            log.info("Список пользователей");
            return jdbcTemplate.query(selectSql, USER_MAPPER);
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public User add(User user) {

        String sqlQuery = "INSERT INTO USERS (email, name, login, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        return new User(keyHolder.getKey().longValue(), user.getEmail(), user.getName(), user.getLogin(), user.getBirthday());
    }

    @Override
    public void put(User user) {

        String selectSql = "UPDATE USERS SET EMAIL = ?, NAME = ?, LOGIN = ?, BIRTHDAY = ? where USER_ID = ?";

        jdbcTemplate.update(selectSql, user.getEmail(), user.getName(), user.getLogin(), java.sql.Date.valueOf(user.getBirthday()), user.getId());

    }

    @Override
    public void del(User user) {

        String selectSql = "UPDATE USERS SET EMAIL = ?, NAME = ?, LOGIN = ?, BIRTHDAY = ? where USER_ID = ?";

        jdbcTemplate.update(selectSql, user.getId());

    }

    @Override
    public Optional<User> findUserById(Long userId) {

//        String selectSql = "SELECT * FROM USERS WHERE USER_ID = ?";
//
//        User user = jdbcTemplate.queryForObject(selectSql, USER_MAPPER, userId);
//
//        try {
//            log.info("Найден пользователь: {} {}" , user.getId(), user.getName());
//            return Optional.of(user);
//        } catch (RuntimeException e) {
//            log.info("Пользователь с идентификатором {} не найден.", userId);
//            return Optional.empty();
//        }

        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", userId);

        // обрабатываем результат выполнения запроса
        if(userRows.next()) {
            User user = new User(
                    userId,
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());

            log.info("Найден пользователь: {} {}" , user.getId(), user.getName());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            return Optional.empty();
        }
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