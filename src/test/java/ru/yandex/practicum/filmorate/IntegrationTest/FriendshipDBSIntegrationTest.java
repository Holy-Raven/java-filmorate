package ru.yandex.practicum.filmorate.IntegrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
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
public class FriendshipDBSIntegrationTest {

    final UserStorage userStorage;
    final FriendshipStorage friendshipStorage;

    static User user_1;
    static User user_2;
    static User user_3;
    static User userDB_1;
    static User userDB_2;
    static User userDB_3;

    @BeforeEach
    void beforeEach() {

        user_1 = new User(null, "email@user_1", "name_user_1", "login_user_1", (LocalDate.of(1995, 5, 5)));
        user_2 = new User(null, "email@user_2", "name_user_2", "login_user_2", (LocalDate.of(2000, 3, 2)));
        user_3 = new User(null, "email@user_3", "name_user_3", "login_user_3", (LocalDate.of(2005, 8, 4)));

    }

    @AfterEach
    void afterEach() {
        for (User user : userStorage.allUsers()) {
            userStorage.del(user);
        }
    }
    @Test
    void testAddAndFindFriends() {

        userDB_1 = userStorage.add(user_1);
        userDB_2 = userStorage.add(user_2);
        userDB_3 = userStorage.add(user_3);

        final Friendship friendship_1_2 = new Friendship(userDB_1.getId(), userDB_2.getId());
        final Friendship friendship_1_3 = new Friendship(userDB_1.getId(), userDB_3.getId());

        friendshipStorage.add(friendship_1_2);
        friendshipStorage.add(friendship_1_3);

        final List<Long> friendsListUser_1 = friendshipStorage.findAllById(userDB_1.getId());
        final List<Long> friendsListUser_2 = friendshipStorage.findAllById(userDB_2.getId());
        final List<Long> friendsListUser_3 = friendshipStorage.findAllById(userDB_3.getId());

        assertTrue(friendsListUser_1.contains(userDB_2.getId()));
        assertTrue(friendsListUser_1.contains(userDB_3.getId()));
        assertTrue(friendsListUser_2.isEmpty());
        assertTrue(friendsListUser_3.isEmpty());

    }
    @Test
    void testPutAndCheckStatusFriends() {

        userDB_1 = userStorage.add(user_1);
        userDB_2 = userStorage.add(user_2);
        userDB_3 = userStorage.add(user_3);

        final Friendship friendship_1_2 = new Friendship(userDB_1.getId(), userDB_2.getId());
        final Friendship friendship_2_1 = new Friendship(userDB_2.getId(), userDB_1.getId());
        final Friendship friendship_1_3 = new Friendship(userDB_1.getId(), userDB_3.getId());

        friendshipStorage.add(friendship_1_2);
        friendshipStorage.add(friendship_2_1);
        friendshipStorage.add(friendship_1_3);

        friendshipStorage.put(friendship_1_2);

        assertTrue(friendshipStorage.status(friendship_1_2));
        assertTrue(friendshipStorage.status(friendship_2_1));
        assertFalse(friendshipStorage.status(friendship_1_3));


    }
    @Test
    void testFindFriendshipById() {

        userDB_1 = userStorage.add(user_1);
        userDB_2 = userStorage.add(user_2);

        final Friendship friendship = new Friendship(userDB_1.getId(), userDB_2.getId());

        friendshipStorage.add(friendship);

        final Optional<Friendship> optionalFriendship = friendshipStorage.findFriendship(friendship);

        assertThat(optionalFriendship)
                .hasValueSatisfying(friendShip ->
                        assertThat(friendShip)
                                .hasFieldOrPropertyWithValue("first_user_id", userDB_1.getId())
                                .hasFieldOrPropertyWithValue("second_user_id", userDB_2.getId()));
    }
    @Test
    void testDelFriendsList() {

        userDB_1 = userStorage.add(user_1);
        userDB_2 = userStorage.add(user_2);

        final Friendship friendship = new Friendship(userDB_1.getId(), userDB_2.getId());

        friendshipStorage.add(friendship);

        assertThat(friendshipStorage.findFriendship(friendship)).isPresent();

        friendshipStorage.del(friendship);

        assertThat(friendshipStorage.findFriendship(friendship)).isNotPresent();
    }

}
