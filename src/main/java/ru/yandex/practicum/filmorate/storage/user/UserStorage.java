package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> allUsers();

    Collection<Long> keyUsers();

    User add(User user);

    User put(User user);

    User del(User User);

}
