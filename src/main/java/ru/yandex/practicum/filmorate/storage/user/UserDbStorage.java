package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

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

        String email = user.getEmail();
        String name = user.getName();
        String login = user.getEmail();
        Date birthday = java.sql.Date.valueOf(user.getBirthday());

        this.jdbcTemplate.update(
                "INSERT INTO USERS AS U (email, name, login, birthday) VALUES (?, ?, ?, ?, " +
                    email,name,login,birthday );
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

        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS AS U where U.USER_ID = ?", userId);

        // обрабатываем результат выполнения запроса
        if(userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    LocalDate.ofInstant(userRows.getDate("birthday").toInstant(), ZoneId.systemDefault()));

            log.info("Найден пользователь: {} {}" , user.getId(), user.getName());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            return null;
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
