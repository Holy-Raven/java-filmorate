
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    public static UserController userController;

    @BeforeEach
    public void beforeEach() {

        final UserService userService = new UserService(new InMemoryUserStorage());

        userController = new UserController(userService);

        userService.setNewId(1);

    }

    @Test
    public void addCorrectedUser() {

        User user = User.builder()
                .id(null)
                .email("ragnar@gmail.com")
                .name("Ragnar")
                .login("LodBrok")
                .birthday(LocalDate.of(795, 11, 17))
                .build();

        User userOutBase = userController.create(user);

        assertFalse(userController.findAll().isEmpty());
        assertEquals(1, userController.findAll().size());
        assertEquals(1, userOutBase.getId());

    }

    @Test
    public void addUserNotValidEmail()  {


        // емайл введен не в некорректном формате
        User user = User.builder()
                .id(null)
                .email("harald_gmail.com")
                .name("Harald")
                .login("Brave")
                .birthday(LocalDate.of(1022, 01, 10))
                .build();

        assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user);
                    throw new ValidationException("Your email cannot be empty and must contain the character @");
                }
        );

        assertTrue(userController.findAll().isEmpty());

        // емайл пустой
        User user2 = User.builder()
                .id(null)
                .email(null)
                .name("Harald")
                .login("Brave")
                .birthday(LocalDate.of(1022, 01, 10))
                .build();

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

        // логин введен не в некорректном формате
        User user = User.builder()
                .id(null)
                .email("richard@gmail.com")
                .name("Richard")
                .login("Lion Heart")
                .birthday(LocalDate.of(1157, 8, 9))
                .build();

        assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user);
                    throw new ValidationException("Your login cannot be empty or contain spaces.");
                }
        );

        assertTrue(userController.findAll().isEmpty());

        // логин пустой
        User user2 = User.builder()
                .id(null)
                .email("richard@gmail.com")
                .name("Richard")
                .login(null)
                .birthday(LocalDate.of(1157, 8, 9))
                .build();

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

        User user = User.builder()
                .id(null)
                .email("rurik@yandex.ru")
                .name(null)
                .login("Rurik")
                .birthday(LocalDate.of(879, 7, 11))
                .build();

        User userOutBase = userController.create(user);

        assertFalse(userController.findAll().isEmpty());
        assertEquals(1, userController.findAll().size());
        assertEquals(1, userOutBase.getId());
        assertEquals("Rurik", userOutBase.getName());
    }

    @Test
    public void addUserFutureBirthday()  {

        User user = User.builder()
                .id(null)
                .email("miroslav@yandex.ru")
                .name("Miroslav")
                .login("MJ")
                .birthday(LocalDate.of(2030, 2, 20))
                .build();

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