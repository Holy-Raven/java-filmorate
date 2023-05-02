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
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


    @SpringBootTest
    @AutoConfigureTestDatabase
    @RequiredArgsConstructor(onConstructor__ = @Autowired)
class FilmDBSIntegrationTest {

    final FilmStorage filmStorage;
    static Film film_1;
    static Film film_2;

    @BeforeEach
    void beforeEach() {
        film_1 = new Film(null, "name_film_1", "description_film_1", (LocalDate.of(1995, 5, 5)), 100, new Mpa(1L));
        film_2 = new Film(null, "name_film_2", "description_film_2", (LocalDate.of(2000, 3, 2)), 90, new Mpa(2L));
    }
    @AfterEach
    void afterEach() {
        for (Film film : filmStorage.allFilms()) {
            filmStorage.del(film);
        }
    }
    @Test
    void testAddFilm() {

        final Film dbFilm_1 = filmStorage.add(film_1);

        assertThat(dbFilm_1.getId()).isNotNull();
        assertThat(dbFilm_1.getName()).isEqualTo(film_1.getName());
        assertThat(dbFilm_1.getDescription()).isEqualTo(film_1.getDescription());
        assertThat(dbFilm_1.getReleaseDate()).isEqualTo(film_1.getReleaseDate());
        assertThat(dbFilm_1.getDuration()).isEqualTo(film_1.getDuration());
        assertThat(dbFilm_1.getMpa()).isEqualTo(film_1.getMpa());
    }
    @Test
    void testPutFilm() {

        final Film dbFilm_1 = filmStorage.add(film_1);

        final long id = dbFilm_1.getId();

        Film testFilm_2 = new Film(id,
                film_2.getName(),
                film_2.getDescription(),
                film_2.getReleaseDate(),
                film_2.getDuration(),
                film_2.getMpa());

        filmStorage.put(testFilm_2);

        final Optional<Film> filmOptional = filmStorage.findFilmById(id);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", id)
                                .hasFieldOrPropertyWithValue("name", film_2.getName())
                                .hasFieldOrPropertyWithValue("description", film_2.getDescription())
                                .hasFieldOrPropertyWithValue("releaseDate", film_2.getReleaseDate())
                                .hasFieldOrPropertyWithValue("duration", film_2.getDuration())
                                .hasFieldOrPropertyWithValue("mpa", film_2.getMpa())
                );
    }
    @Test
    void testFindFilmById() {

        final long id = filmStorage.add(film_1).getId();

        final Optional<Film> filmOptional = filmStorage.findFilmById(id);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", id)
                                        .hasFieldOrPropertyWithValue("name", film_1.getName())
                                        .hasFieldOrPropertyWithValue("description", film_1.getDescription())
                                        .hasFieldOrPropertyWithValue("releaseDate", film_1.getReleaseDate())
                                        .hasFieldOrPropertyWithValue("duration", film_1.getDuration())
                                        .hasFieldOrPropertyWithValue("mpa", film_1.getMpa())
                );
    }
    @Test
    void testFindAll() {
        final Film dbFilm_1 = filmStorage.add(film_1);
        final Film dbFilm_2 = filmStorage.add(film_2);

        final List<Film> allFilms = filmStorage.allFilms();

        assertThat(allFilms).isNotNull();
        assertThat(allFilms.size()).isEqualTo(2);
        assertTrue(allFilms.contains(dbFilm_1));
        assertTrue(allFilms.contains(dbFilm_2));
    }
    @Test
    void testDelFilm() {

        final Film dbFilm_1 = filmStorage.add(film_1);

        final long id = dbFilm_1.getId();

        filmStorage.del(dbFilm_1);

        final Optional<Film> filmOptional = filmStorage.findFilmById(id);

        assertFalse(filmOptional.isPresent());

    }



}