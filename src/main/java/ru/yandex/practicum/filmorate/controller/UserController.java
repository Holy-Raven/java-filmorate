package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceInterface;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserServiceInterface userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // получение списка всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    // создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    // обновление пользователя
    @PutMapping
    public User put(@RequestBody User user) {
        return userService.update(user);
    }

    // удаление пользователя
    @DeleteMapping
    public User del(@RequestBody User user) {
        return userService.delete(user);
    }

    // найти юзера по id
    @GetMapping("/{userId}")
    public Optional<User> findUserById(@PathVariable("userId") String userId) {
        return userService.findUserById(userId);
    }

    // добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") String id,
                          @PathVariable("friendId") String friendId) {
        return userService.addFriends(id, friendId);
    }

    // удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public User delFriend(@PathVariable("id") String id,
                          @PathVariable("friendId") String friendId) {
        return userService.delFriends(id, friendId);
    }

    // возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> allUserFriends(@PathVariable("id") String id) {
        return userService.friendsList(id);
    }

    // список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable("id") String id,
                                    @PathVariable("otherId") String otherId) {
        return userService.commonFriends(id, otherId);
    }
}
