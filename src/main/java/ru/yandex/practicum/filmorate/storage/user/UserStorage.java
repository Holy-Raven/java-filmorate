package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Long, User> allUsers();

    User add(User user);

    User put(User user);

    User del(User user);

    User findUserById(Long userId);

    User addFriends(Long user1, Long user2);

    User delFriends(Long user1, Long user2);

    List<User> friendsList(Long user);

}
