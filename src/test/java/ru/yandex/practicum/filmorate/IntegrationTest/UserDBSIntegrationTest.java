package ru.yandex.practicum.filmorate.IntegrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class UserDBSIntegrationTest {

    final UserStorage userStorage;
    static User user_1;
    static User user_2;

    @BeforeEach
    void beforeEach() {

        user_1 = new User(null, "email@user_1", "name_user_1", "login_user_1", (LocalDate.of(1995, 5, 5)));
        user_2 = new User(null, "email@user_2", "name_user_2", "login_user_2", (LocalDate.of(2000, 3, 2)));

    }
    @AfterEach
    void afterEach() {
        for (User user : userStorage.allUsers()) {
            userStorage.del(user);
        }
    }

    @Test
    void testAddUser() {

        final User dbUser_1 = userStorage.add(user_1);

        assertThat(dbUser_1.getId()).isNotNull();
        assertThat(dbUser_1.getEmail()).isEqualTo(user_1.getEmail());
        assertThat(dbUser_1.getName()).isEqualTo(user_1.getName());
        assertThat(dbUser_1.getLogin()).isEqualTo(user_1.getLogin());
        assertThat(dbUser_1.getBirthday()).isEqualTo(user_1.getBirthday());
    }
    @Test
    void testPutUser() {

        final User dbUser_1 = userStorage.add(user_1);

        final long id = dbUser_1.getId();

        User testUser_2 = new User(id,
                user_2.getEmail(),
                user_2.getName(),
                user_2.getLogin(),
                user_2.getBirthday());

        userStorage.put(testUser_2);

        final Optional<User> userOptional = userStorage.findUserById(id);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", id)
                                .hasFieldOrPropertyWithValue("email", user_2.getEmail())
                                .hasFieldOrPropertyWithValue("name", user_2.getName())
                                .hasFieldOrPropertyWithValue("login", user_2.getLogin())
                                .hasFieldOrPropertyWithValue("birthday", user_2.getBirthday())
                );
    }
    @Test
    void testFindUserById() {

        final long id = userStorage.add(user_1).getId();

        final Optional<User> userOptional = userStorage.findUserById(id);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", id)
                                .hasFieldOrPropertyWithValue("email", user_1.getEmail())
                                .hasFieldOrPropertyWithValue("name", user_1.getName())
                                .hasFieldOrPropertyWithValue("login", user_1.getLogin())
                                .hasFieldOrPropertyWithValue("birthday", user_1.getBirthday())
                );
    }
    @Test
    void testFindAll() {

        final User dbUser_1 = userStorage.add(user_1);
        final User dbUser_2 = userStorage.add(user_2);

        final List<User> allUsers = userStorage.allUsers();

        assertThat(allUsers).isNotNull();
        assertThat(allUsers.size()).isEqualTo(2);
        assertTrue(allUsers.contains(dbUser_1));
        assertTrue(allUsers.contains(dbUser_2));
    }
    @Test
    void testDelUser() {

        final User dbUser_1 = userStorage.add(user_1);

        final long id = dbUser_1.getId();

        userStorage.del(dbUser_1);

        final Optional<User> userOptional = userStorage.findUserById(id);

        assertFalse(userOptional.isPresent());
    }
}