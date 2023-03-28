package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserServiceInterface {

    Collection<User> findAll();

    User create(User user);

    User update(User user);

    User delete(User user);

    User findUserById(String userId);

}
