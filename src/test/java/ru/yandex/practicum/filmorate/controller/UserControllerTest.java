
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    public static UserController userController;

    @BeforeEach
    public void beforeEach () {

        final UserService userService = new UserService(new InMemoryUserStorage());

        userController = new UserController(userService);

        UserService.setNewId(1);

    }

    @Test
    public void addCorrectedUser() {

        LocalDate birthDay = LocalDate.of(795, 11, 17);

        User user = new User(null, "ragnar@gmail.com", "Ragnar", "LodBrok", birthDay);

        User userOutBase = userController.create(user);

        assertFalse(userController.findAll().isEmpty());
        assertEquals(1, userController.findAll().size());
        assertEquals(1, userOutBase.getId());

    }

    @Test
    public void addUserNotValidEmail()  {

        LocalDate birthDay = LocalDate.of(1022,01,10);

        // емайл введен не в некорректном формате
        User user = new User(null, "harald_gmail.com", "Harald", "Brave", birthDay);

        assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user);
                    throw new ValidationException("Your email cannot be empty and must contain the character @");
                }
        );

        assertTrue(userController.findAll().isEmpty());

        // емайл пустой
        User user2 = new User(null, null, "Harald", "Brave", birthDay);

        assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user2);
                    throw new ValidationException("Your email cannot be empty and must contain the character @");
                }
        );

        assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void addUserNotValidLogin()  {

        LocalDate birthDay = LocalDate.of(1157,8,9);

        // логин введен не в некорректном формате
        User user = new User(null, "richard@gmail.com", "Richard", "Lion Heart", birthDay);

        assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user);
                    throw new ValidationException("Your login cannot be empty or contain spaces.");
                }
        );

        assertTrue(userController.findAll().isEmpty());

        // логин пустой
        User user2 = new User(null, "richard@gmail.com", "Richard", null, birthDay);

        assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user2);
                    throw new ValidationException("Your login cannot be empty or contain spaces.");
                }
        );

        assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void addUserEmptyName() {

        LocalDate birthDay = LocalDate.of(879, 07, 11);

        User user = new User(null, "rurik@yandex.ru", null, "Rurik", birthDay);

        User userOutBase = userController.create(user);

        assertFalse(userController.findAll().isEmpty());
        assertEquals(1, userController.findAll().size());
        assertEquals(1, userOutBase.getId());
        assertEquals("Rurik", userOutBase.getName());
    }

    @Test
    public void addUserFutureBirthday()  {

        LocalDate birthDay = LocalDate.of(2030,02,20);

        User user = new User(null, "miroslav@yandex.ru", "Miroslav", "MJ", birthDay);

        assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user);
                    throw new ValidationException("The date of birth cannot be in the future.");
                }
        );

        assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void getAllUsers() {

        LocalDate birthDay1 = LocalDate.of(795, 11, 17);
        User user1 = new User(null, "ragnar@gmail.com", "Ragnar", "LodBrok", birthDay1);
        User userOutBase1 = userController.create(user1);
        assertEquals(1, userOutBase1.getId());

        LocalDate birthDay2 = LocalDate.of(1022,01,10);
        User user2 = new User(null, "harald@gmail.com", "Harald", "Brave", birthDay2);
        User userOutBase2 = userController.create(user2);
        assertEquals(2, userOutBase2.getId());

        LocalDate birthDay3 = LocalDate.of(1157,8,9);
        User user3 = new User(null, "richard@gmail.com", "Richard", "LionHeart", birthDay3);
        User userOutBase3 = userController.create(user3);
        assertEquals(3, userOutBase3.getId());

        LocalDate birthDay4 = LocalDate.of(879, 07, 11);
        User user4 = new User(null, "rurik@yandex.ru", "Rurik", "Rurik", birthDay4);
        User userOutBase4 = userController.create(user4);
        assertEquals(4, userOutBase4.getId());

        LocalDate birthDay5 = LocalDate.of(2016,11,02);
        User user5 = new User(null, "miroslav@yandex.ru", "Miroslav", "MJ", birthDay5);
        User userOutBase5 = userController.create(user5);
        assertEquals(5, userOutBase5.getId());

        assertEquals(5, userController.findAll().size());
    }

}