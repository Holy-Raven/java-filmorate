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
    static User user1;
    static User user2;

    @BeforeEach
    void beforeEach() {

        user1 = new User(null, "email@user_1", "name_user_1", "login_user_1", (LocalDate.of(1995, 5, 5)));
        user2 = new User(null, "email@user_2", "name_user_2", "login_user_2", (LocalDate.of(2000, 3, 2)));

    }
    @AfterEach
    void afterEach() {
        for (User user : userStorage.allUsers()) {
            userStorage.del(user);
        }
    }

    @Test
    void testAddUser() {

        final User dbUser1 = userStorage.add(user1);

        assertThat(dbUser1.getId()).isNotNull();
        assertThat(dbUser1.getEmail()).isEqualTo(user1.getEmail());
        assertThat(dbUser1.getName()).isEqualTo(user1.getName());
        assertThat(dbUser1.getLogin()).isEqualTo(user1.getLogin());
        assertThat(dbUser1.getBirthday()).isEqualTo(user1.getBirthday());
    }

    @Test
    void testPutUser() {

        final User dbUser1 = userStorage.add(user1);

        final long id = dbUser1.getId();

        User testUser2 = new User(id,
                user2.getEmail(),
                user2.getName(),
                user2.getLogin(),
                user2.getBirthday());

        userStorage.put(testUser2);

        final Optional<User> userOptional = userStorage.findUserById(id);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", id)
                                .hasFieldOrPropertyWithValue("email", user2.getEmail())
                                .hasFieldOrPropertyWithValue("name", user2.getName())
                                .hasFieldOrPropertyWithValue("login", user2.getLogin())
                                .hasFieldOrPropertyWithValue("birthday", user2.getBirthday())
                );
    }

    @Test
    void testFindUserById() {

        final long id = userStorage.add(user1).getId();

        final Optional<User> userOptional = userStorage.findUserById(id);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", id)
                                .hasFieldOrPropertyWithValue("email", user1.getEmail())
                                .hasFieldOrPropertyWithValue("name", user1.getName())
                                .hasFieldOrPropertyWithValue("login", user1.getLogin())
                                .hasFieldOrPropertyWithValue("birthday", user1.getBirthday())
                );
    }

    @Test
    void testFindAll() {

        final User dbUser1 = userStorage.add(user1);
        final User dbUser2 = userStorage.add(user2);

        final List<User> allUsers = userStorage.allUsers();

        assertThat(allUsers).isNotNull();
        assertThat(allUsers.size()).isEqualTo(2);
        assertTrue(allUsers.contains(dbUser1));
        assertTrue(allUsers.contains(dbUser2));
    }

    @Test
    void testDelUser() {

        final User dbUser1 = userStorage.add(user1);

        final long id = dbUser1.getId();

        userStorage.del(dbUser1);

        final Optional<User> userOptional = userStorage.findUserById(id);

        assertFalse(userOptional.isPresent());
    }
}