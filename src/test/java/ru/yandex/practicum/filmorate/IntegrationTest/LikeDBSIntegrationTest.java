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

    static Film film1;
    static User user1;
    static User user2;

    @BeforeEach
    void beforeEach() {
        film1 = new Film(null, "name_film_1", "description_film_1", (LocalDate.of(1995, 5, 5)), 100, new Mpa(1L));
        user1 = new User(null, "email@user_1", "name_user_1", "login_user_1", (LocalDate.of(1995, 5, 5)));
        user2 = new User(null, "email@user_2", "name_user_2", "login_user_2", (LocalDate.of(2000, 3, 2)));
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

        final Film dbFilm1 = filmStorage.add(film1);

        final User dbUser1 = userStorage.add(user1);
        final User dbUser2 = userStorage.add(user2);

        likeStorage.addLikeFilm(dbFilm1.getId(), dbUser1.getId());
        likeStorage.addLikeFilm(dbFilm1.getId(), dbUser2.getId());

        final List<Long> likeList = likeStorage.findLikesListFilmById(dbFilm1.getId());

        assertThat(likeList.size()).isEqualTo(2);
        assertThat(likeList.get(0)).isEqualTo(dbUser1.getId());
        assertThat(likeList.get(1)).isEqualTo(dbUser2.getId());

        likeStorage.delLikeFilm(dbFilm1.getId(), dbUser1.getId());

        final List<Long> afterDellikeList = likeStorage.findLikesListFilmById(dbFilm1.getId());

        assertThat(afterDellikeList.size()).isEqualTo(1);
        assertFalse(afterDellikeList.contains(dbUser1.getId()));
        assertThat(afterDellikeList.get(0)).isEqualTo(dbUser2.getId());

    }

}
