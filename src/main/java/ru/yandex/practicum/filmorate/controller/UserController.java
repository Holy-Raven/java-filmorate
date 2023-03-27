package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.MyValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

//    public final Map<Integer, User> users = new HashMap<>();
//
//    private static int newId = 1;
//    private static int getNewId() {
//        return newId++;
//    }
//    public static void setNewId(int newId) {
//        UserController.newId = newId;
//    }

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // получение списка всех пользователей.
    @GetMapping
    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    // создание пользователя.
    @PostMapping
    public User create(@Valid @RequestBody User user) {
//
//        LocalDate currentTime = LocalDate.now();
//
//        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
//            log.error("Your email cannot be empty and must contain the character @");
//            throw new MyValidationException("Your email cannot be empty and must contain the character @");
//
//        } else if (user.getLogin() == null || user.getEmail().isBlank() || user.getLogin().contains(" ")) {
//            log.error("Your login cannot be empty or contain spaces.");
//            throw new MyValidationException("Your login cannot be empty or contain spaces.");
//
//        }  else if (user.getBirthday().isAfter(currentTime)) {
//            log.error("The date of birth cannot be in the future.");
//            throw new MyValidationException("The date of birth cannot be in the future.");
//        }
//
//        if (user.getName() == null || user.getName().isBlank()) {
//            log.info("The name to display cannot be empty. You have been assigned a name: {}", user.getLogin());
//            user = new User(getNewId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
//
//        } else {
//            user = new User(getNewId(), user.getEmail(), user.getName(), user.getLogin(), user.getBirthday());
//        }
//
//        users.put(user.getId(), user);
//        log.info("User added : {}", user.getLogin());
//        return user;
        return inMemoryUserStorage.create(user);
    }

    // обновление пользователя.
    @PutMapping
    public User put(@RequestBody User user) {
//        if (users.containsKey(user.getId())){
//            users.put(user.getId(), user);
//        } else {
//            log.error("There is no such user in our list of users");
//            throw new MyValidationException("There is no such user in our list of users");
//        }
//        log.info("User data updated: {}", user.getLogin());
//        return user;
        return inMemoryUserStorage.put(user);
    }




}
