package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
public class UserService implements UserServiceInterface {

    UserStorage userStorage;

    private static long newId = 1;
    private static long getNewId() {
        return newId++;
    }
    public static void setNewId(int newId) {
        UserService.newId = newId;
    }

    @Autowired
    UserService(InMemoryUserStorage userStorage){
        this.userStorage = userStorage;
    }

    @Override
    public Collection<User> findAll() {

        return userStorage.allUsers().values();
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

        log.info("User added : {}", user.getLogin());
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {

        if (userStorage.allUsers().containsKey(user.getId())){
            userStorage.put(user);
        } else {
            log.error("There is no such user in our list of users");
            throw new MyValidationException("There is no such user in our list of users");
        }
        log.info("User data updated: {}", user.getLogin());
        return user;
     }

    @Override
    public User delete(User user) {

        if (userStorage.allUsers().containsKey(user.getId())){
            userStorage.del(user);
        } else {
            log.error("There is no such user in our list of users");
            throw new MyValidationException("There is no such user in our list of users");
        }
        log.info("User deleted: {}", user.getLogin());
        return user;
    }
}
