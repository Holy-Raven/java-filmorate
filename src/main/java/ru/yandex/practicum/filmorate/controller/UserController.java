package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // получение списка всех пользователей.
    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    // создание пользователя.
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    // обновление пользователя.
    @PutMapping
    public User put(@RequestBody User user) {
        return userService.update(user);
    }




}
