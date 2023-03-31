package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserServiceInterface {

    Collection<User> findAll();

    User create(User user);

    User update(User user);

    User delete(User user);

    User findUserById(String userId);

    User addFriends(String user1, String user2);

    User delFriends(String user1, String user2);

    List<User> friendsList(String user);

    List<User> commonFriends(String user1, String user2);

}
