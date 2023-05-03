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

    static User user1;
    static User user2;
    static User user3;
    static User userDB1;
    static User userDB2;
    static User userDB3;

    @BeforeEach
    void beforeEach() {

        user1 = new User(null, "email@user_1", "name_user_1", "login_user_1", (LocalDate.of(1995, 5, 5)));
        user2 = new User(null, "email@user_2", "name_user_2", "login_user_2", (LocalDate.of(2000, 3, 2)));
        user3 = new User(null, "email@user_3", "name_user_3", "login_user_3", (LocalDate.of(2005, 8, 4)));

    }
    @AfterEach
    void afterEach() {
        for (User user : userStorage.allUsers()) {
            userStorage.del(user);
        }
    }

    @Test
    void testAddAndFindFriends() {

        userDB1 = userStorage.add(user1);
        userDB2 = userStorage.add(user2);
        userDB3 = userStorage.add(user3);

        final Friendship friendship12 = new Friendship(userDB1.getId(), userDB2.getId());
        final Friendship friendship13 = new Friendship(userDB1.getId(), userDB3.getId());

        friendshipStorage.add(friendship12);
        friendshipStorage.add(friendship13);

        final List<Long> friendsListUser1 = friendshipStorage.findAllById(userDB1.getId());
        final List<Long> friendsListUser2 = friendshipStorage.findAllById(userDB2.getId());
        final List<Long> friendsListUser3 = friendshipStorage.findAllById(userDB3.getId());

        assertTrue(friendsListUser1.contains(userDB2.getId()));
        assertTrue(friendsListUser1.contains(userDB3.getId()));
        assertTrue(friendsListUser2.isEmpty());
        assertTrue(friendsListUser3.isEmpty());
    }

    @Test
    void testPutAndCheckStatusFriends() {

        userDB1 = userStorage.add(user1);
        userDB2 = userStorage.add(user2);
        userDB3 = userStorage.add(user3);

        final Friendship friendship12 = new Friendship(userDB1.getId(), userDB2.getId());
        final Friendship friendship21 = new Friendship(userDB2.getId(), userDB1.getId());
        final Friendship friendship13 = new Friendship(userDB1.getId(), userDB3.getId());

        friendshipStorage.add(friendship12);
        friendshipStorage.add(friendship21);
        friendshipStorage.add(friendship13);

        friendshipStorage.put(friendship12);

        assertTrue(friendshipStorage.status(friendship12));
        assertTrue(friendshipStorage.status(friendship21));
        assertFalse(friendshipStorage.status(friendship13));
    }

    @Test
    void testFindFriendshipById() {

        userDB1 = userStorage.add(user1);
        userDB2 = userStorage.add(user2);

        final Friendship friendship = new Friendship(userDB1.getId(), userDB2.getId());

        friendshipStorage.add(friendship);

        final Optional<Friendship> optionalFriendship = friendshipStorage.findFriendship(friendship);

        assertThat(optionalFriendship)
                .hasValueSatisfying(friendShip ->
                        assertThat(friendShip)
                                .hasFieldOrPropertyWithValue("firstUserId", userDB1.getId())
                                .hasFieldOrPropertyWithValue("secondUserId", userDB2.getId()));
    }
    @Test
    void testDelFriendsList() {

        userDB1 = userStorage.add(user1);
        userDB2 = userStorage.add(user2);

        final Friendship friendship = new Friendship(userDB1.getId(), userDB2.getId());

        friendshipStorage.add(friendship);

        assertThat(friendshipStorage.findFriendship(friendship)).isPresent();

        friendshipStorage.del(friendship);

        assertThat(friendshipStorage.findFriendship(friendship)).isNotPresent();
    }

}
