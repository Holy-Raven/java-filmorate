package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    List<User> findAll();

    User create(User user);

    User update(User user);

    User delete(User user);

    Optional<User> findUserById(String userId);

    User addFriends(String user1, String user2);

    User delFriends(String user1, String user2);

    List<User> friendsList(String user);

    List<User> commonFriends(String user1, String user2);

}
