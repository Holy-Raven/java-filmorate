
package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.MyValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final Map<Integer, User> users = new HashMap<>();

    private static int newId = 1;
    private static int getNewId() {
        return newId++;
    }
    public static void setNewId(int newId) {
        UserController.newId = newId;
    }

    // получение списка всех пользователей.
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    // создание пользователя.
    @PostMapping
    public User create(@Valid @RequestBody User user) {

        //раньше было так (сняла комментарии, что бы тесты написать)
        LocalDate currentTime = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ваша электронная почта не может быть пустой и должна содержать символ @");
            throw new MyValidationException("Ваша электронная почта не может быть пустой и должна содержать символ @");

        } else if (user.getLogin() == null || user.getEmail().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ваш логин не может быть пустым или содержать пробелы.");
            throw new MyValidationException("Ваш логин не может быть пустым или содержать пробелы.");

        }  else if (user.getBirthday().isAfter(currentTime)) {
            log.error("Дата рождения не может быть в будущем.");
            throw new MyValidationException("Дата рождения не может быть в будущем.");

        }
        // без всего того что выше в postman и так работает

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя для отображения не может быть пустым. Вам назначено имя: {}", user.getLogin());
            user = new User(getNewId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());

        } else {
            user = new User(getNewId(), user.getEmail(), user.getName(), user.getLogin(), user.getBirthday());
        }

        users.put(user.getId(), user);
        log.info("Добавлен юзер: {}", user.getLogin());
        return user;
    }

    // обновление пользователя.
    @PutMapping
    public User put(@RequestBody User user) {
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
        } else {
            log.error("Такого юзера нет в нашем списке юзеров");
            throw new MyValidationException("Такого юзера нет в нашем списке юзеров");
        }
        log.info("Обновлены данные юзера: {}", user.getLogin());
        return user;
    }
}
