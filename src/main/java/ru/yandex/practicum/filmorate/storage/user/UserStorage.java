package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    Map<Long, User> allUsers();

    User add(User user);

    User put(User user);

    User del(User user);

}
