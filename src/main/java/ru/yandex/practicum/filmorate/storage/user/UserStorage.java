package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> allUsers();

    User add(User user);

    void put(User user);

    void del(User user);

    Optional<User> findUserById(Long userId);

    boolean existsById(Long id);
}
