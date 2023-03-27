package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.MyValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    public final Map<Integer, User> users = new HashMap<>();

    private static int newId = 1;
    private static int getNewId() {
        return newId++;
    }
    public static void setNewId(int newId) {
        InMemoryUserStorage.newId = newId;
    }



    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {

        LocalDate currentTime = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Your email cannot be empty and must contain the character @");
            throw new MyValidationException("Your email cannot be empty and must contain the character @");

        } else if (user.getLogin() == null || user.getEmail().isBlank() || user.getLogin().contains(" ")) {
            log.error("Your login cannot be empty or contain spaces.");
            throw new MyValidationException("Your login cannot be empty or contain spaces.");

        }  else if (user.getBirthday().isAfter(currentTime)) {
            log.error("The date of birth cannot be in the future.");
            throw new MyValidationException("The date of birth cannot be in the future.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("The name to display cannot be empty. You have been assigned a name: {}", user.getLogin());
            user = new User(getNewId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());

        } else {
            user = new User(getNewId(), user.getEmail(), user.getName(), user.getLogin(), user.getBirthday());
        }

        users.put(user.getId(), user);
        log.info("User added : {}", user.getLogin());
        return user;
    }

    @Override
    public User put(User user) {
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
        } else {
            log.error("There is no such user in our list of users");
            throw new MyValidationException("There is no such user in our list of users");
        }
        log.info("User data updated: {}", user.getLogin());
        return user;
    }

    @Override
    public User del(User User) {
        return null;
    }
}
