package ru.yandex.practicum.filmorate.IntegrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class LikeDBSIntegrationTest {

    final LikeStorage likeStorage;
    final FilmStorage filmStorage;
    final UserStorage userStorage;

    static Film film_1;
    static User user_1;
    static User user_2;

    @BeforeEach
    void beforeEach() {

        film_1 = new Film(null, "name_film_1", "description_film_1", (LocalDate.of(1995, 5, 5)), 100, new Mpa(1L));
        user_1 = new User(null, "email@user_1", "name_user_1", "login_user_1", (LocalDate.of(1995, 5, 5)));
        user_2 = new User(null, "email@user_2", "name_user_2", "login_user_2", (LocalDate.of(2000, 3, 2)));

    }
    @AfterEach
    void afterEach() {

        for (Film film : filmStorage.allFilms()) {
            filmStorage.del(film);
        }

        for (User user : userStorage.allUsers()) {
            userStorage.del(user);
        }

    }
    @Test
    void testLikeFilm() {

        final Film dbFilm_1 = filmStorage.add(film_1);

        final User dbUser_1 = userStorage.add(user_1);
        final User dbUser_2 = userStorage.add(user_2);

        likeStorage.addLikeFilm(dbFilm_1.getId(), dbUser_1.getId());
        likeStorage.addLikeFilm(dbFilm_1.getId(), dbUser_2.getId());

        final List<Long> likeList = likeStorage.findLikesListFilmById(dbFilm_1.getId());

        assertThat(likeList.size()).isEqualTo(2);
        assertThat(likeList.get(0)).isEqualTo(dbUser_1.getId());
        assertThat(likeList.get(1)).isEqualTo(dbUser_2.getId());

        likeStorage.delLikeFilm(dbFilm_1.getId(), dbUser_1.getId());

        final List<Long> afterDellikeList = likeStorage.findLikesListFilmById(dbFilm_1.getId());

        assertThat(afterDellikeList.size()).isEqualTo(1);
        assertFalse(afterDellikeList.contains(dbUser_1.getId()));
        assertThat(afterDellikeList.get(0)).isEqualTo(dbUser_2.getId());

    }

}
