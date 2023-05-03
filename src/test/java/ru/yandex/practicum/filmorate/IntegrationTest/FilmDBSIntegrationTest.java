package ru.yandex.practicum.filmorate.IntegrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

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

    final GenreStorage genreStorage;
    static Film film1;
    static Film film2;

    @BeforeEach
    void beforeEach() {
        film1 = new Film(null, "name_film_1", "description_film_1", (LocalDate.of(1995, 5, 5)), 100, new Mpa(1L));
        film2 = new Film(null, "name_film_2", "description_film_2", (LocalDate.of(2000, 3, 2)), 90, new Mpa(2L));
    }

    @AfterEach
    void afterEach() {
        for (Film film : filmStorage.allFilms()) {
            filmStorage.del(film);
        }
    }

    @Test
    void testAddFilm() {

        final Film dbFilm1 = filmStorage.add(film1);

        assertThat(dbFilm1.getId()).isNotNull();
        assertThat(dbFilm1.getName()).isEqualTo(film1.getName());
        assertThat(dbFilm1.getDescription()).isEqualTo(film1.getDescription());
        assertThat(dbFilm1.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(dbFilm1.getDuration()).isEqualTo(film1.getDuration());
        assertThat(dbFilm1.getMpa()).isEqualTo(film1.getMpa());
    }

    @Test
    void testPutFilm() {

        final Film dbFilm1 = filmStorage.add(film1);

        final long id = dbFilm1.getId();

        Film testFilm2 = new Film(id,
                film2.getName(),
                film2.getDescription(),
                film2.getReleaseDate(),
                film2.getDuration(),
                film2.getMpa());

        filmStorage.put(testFilm2);

        final Optional<Film> filmOptional = filmStorage.findFilmById(id);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", id)
                                .hasFieldOrPropertyWithValue("name", film2.getName())
                                .hasFieldOrPropertyWithValue("description", film2.getDescription())
                                .hasFieldOrPropertyWithValue("releaseDate", film2.getReleaseDate())
                                .hasFieldOrPropertyWithValue("duration", film2.getDuration())
                                .hasFieldOrPropertyWithValue("mpa", film2.getMpa())
                );
    }

    @Test
    void testFindFilmById() {

        final long id = filmStorage.add(film1).getId();

        final Optional<Film> filmOptional = filmStorage.findFilmById(id);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", id)
                                        .hasFieldOrPropertyWithValue("name", film1.getName())
                                        .hasFieldOrPropertyWithValue("description", film1.getDescription())
                                        .hasFieldOrPropertyWithValue("releaseDate", film1.getReleaseDate())
                                        .hasFieldOrPropertyWithValue("duration", film1.getDuration())
                                        .hasFieldOrPropertyWithValue("mpa", film1.getMpa())
                );
    }

    @Test
    void testFindAll() {
        final Film dbFilm1 = filmStorage.add(film1);
        final Film dbFilm2 = filmStorage.add(film2);

        final List<Film> allFilms = filmStorage.allFilms();

        assertThat(allFilms).isNotNull();
        assertThat(allFilms.size()).isEqualTo(2);
        assertTrue(allFilms.contains(dbFilm1));
        assertTrue(allFilms.contains(dbFilm2));
    }

    @Test
    void testDelFilm() {

        final Film dbFilm1 = filmStorage.add(film1);

        final long id = dbFilm1.getId();

        filmStorage.del(dbFilm1);

        final Optional<Film> filmOptional = filmStorage.findFilmById(id);

        assertFalse(filmOptional.isPresent());

    }

    @Test
    void testGenreFilm(){

        final Film dbFilm1 = filmStorage.add(film1);

        final Genre genre1 = genreStorage.findById(1L).get();
        final Genre genre2 = genreStorage.findById(1L).get();

        filmStorage.addGenreToFilm(dbFilm1.getId(), genre1.getId());
        filmStorage.addGenreToFilm(dbFilm1.getId(), genre2.getId());

        List<Genre> genreList = filmStorage.findGenreListFilmById(dbFilm1.getId());

        assertThat(genreList.size()).isEqualTo(2);
        assertThat(genreList.get(0)).isEqualTo(genre1);
        assertThat(genreList.get(1)).isEqualTo(genre2);

        filmStorage.delGenresListFromFilm(dbFilm1.getId());

        genreList = filmStorage.findGenreListFilmById(dbFilm1.getId());

        assertTrue(genreList.isEmpty());

    }
}