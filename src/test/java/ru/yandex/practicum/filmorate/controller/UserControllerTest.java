
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.MyValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    public static UserController userController;

    @BeforeEach
    public void beforeEach (){

        userController = new UserController();
        UserController.setNewId(1);

    }

    @Test
    public void addCorrectedUser() {

        LocalDate birthDay = LocalDate.of(795, 11, 17);

        User user = new User(null, "ragnar@gmail.com", "Ragnar", "LodBrok", birthDay);

        User userOutBase = userController.create(user);

        assertFalse(userController.users.isEmpty());
        assertEquals(1, userController.users.size());
        assertEquals(1, userOutBase.getId());

    }

    @Test
    public void addUserNotValidEmail()  {

        LocalDate birthDay = LocalDate.of(1022,01,10);

        // емайл введен не в некорректном формате
        User user = new User(null, "harald_gmail.com", "Harald", "Brave", birthDay);

        assertThrows(
                MyValidationException.class,
                () -> {
                    userController.create(user);
                    throw new MyValidationException("Ваша электронная почта не может быть пустой и должна содержать символ @");
                }
        );

        assertTrue(userController.users.isEmpty());

        // емайл пустой
        User user2 = new User(null, null, "Harald", "Brave", birthDay);

        assertThrows(
                MyValidationException.class,
                () -> {
                    userController.create(user2);
                    throw new MyValidationException("Ваша электронная почта не может быть пустой и должна содержать символ @");
                }
        );

        assertTrue(userController.users.isEmpty());
    }

    @Test
    public void addUserNotValidLogin()  {

        LocalDate birthDay = LocalDate.of(1157,8,9);

        // логин введен не в некорректном формате
        User user = new User(null, "richard@gmail.com", "Richard", "Lion Heart", birthDay);

        assertThrows(
                MyValidationException.class,
                () -> {
                    userController.create(user);
                    throw new MyValidationException("Ваш логин не может быть пустым или содержать пробелы.");
                }
        );

        assertTrue(userController.users.isEmpty());

        // логин пустой
        User user2 = new User(null, "richard@gmail.com", "Richard", null, birthDay);

        assertThrows(
                MyValidationException.class,
                () -> {
                    userController.create(user2);
                    throw new MyValidationException("Ваш логин не может быть пустым или содержать пробелы.");
                }
        );

        assertTrue(userController.users.isEmpty());
    }

    @Test
    public void addUserEmptyName() {

        LocalDate birthDay = LocalDate.of(879, 07, 11);

        User user = new User(null, "rurik@yandex.ru", null, "Rurik", birthDay);

        User userOutBase = userController.create(user);

        assertFalse(userController.users.isEmpty());
        assertEquals(1, userController.users.size());
        assertEquals(1, userOutBase.getId());
        assertEquals("Rurik", userOutBase.getName());
    }

    @Test
    public void addUserFutureBirthday()  {

        LocalDate birthDay = LocalDate.of(2030,02,20);

        User user = new User(null, "miroslav@yandex.ru", "Miroslav", "MJ", birthDay);

        assertThrows(
                MyValidationException.class,
                () -> {
                    userController.create(user);
                    throw new MyValidationException("Дата рождения не может быть в будущем.");
                }
        );

        assertTrue(userController.users.isEmpty());
    }

    @Test
    public void getAllUsers() {

        LocalDate birthDay_1 = LocalDate.of(795, 11, 17);
        User user_1 = new User(null, "ragnar@gmail.com", "Ragnar", "LodBrok", birthDay_1);
        User userOutBase_1 = userController.create(user_1);
        assertEquals(1, userOutBase_1.getId());

        LocalDate birthDay_2 = LocalDate.of(1022,01,10);
        User user_2 = new User(null, "harald@gmail.com", "Harald", "Brave", birthDay_2);
        User userOutBase_2 = userController.create(user_2);
        assertEquals(2, userOutBase_2.getId());

        LocalDate birthDay_3 = LocalDate.of(1157,8,9);
        User user_3 = new User(null, "richard@gmail.com", "Richard", "LionHeart", birthDay_3);
        User userOutBase_3 = userController.create(user_3);
        assertEquals(3, userOutBase_3.getId());

        LocalDate birthDay_4 = LocalDate.of(879, 07, 11);
        User user_4 = new User(null, "rurik@yandex.ru", "Rurik", "Rurik", birthDay_4);
        User userOutBase_4 = userController.create(user_4);
        assertEquals(4, userOutBase_4.getId());

        LocalDate birthDay_5 = LocalDate.of(2016,11,02);
        User user_5 = new User(null, "miroslav@yandex.ru", "Miroslav", "MJ", birthDay_5);
        User userOutBase_5 = userController.create(user_5);
        assertEquals(5, userOutBase_5.getId());

        assertEquals(5, userController.users.size());
    }

}