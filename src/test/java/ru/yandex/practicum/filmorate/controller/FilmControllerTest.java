
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    public static FilmController filmController;

    @BeforeEach
    public void beforeEach (){

        final FilmService filmService = new FilmService(new InMemoryFilmStorage());

        filmController = new FilmController(filmService);

        FilmService.setNewId(1);

    }

    @Test
    public void addCorrectedFilm()  {

        LocalDate releaseDate = LocalDate.of(1998,06,23);
        Film film = new Film(null, "Карты деньги два ствола", " Один из лучших фильмов Гая Ричи", releaseDate, 107);

        Film filmOutBase = filmController.create(film);

        assertFalse(filmController.findAll().isEmpty());
        assertEquals(1, filmController.findAll().size());
        assertEquals(1, filmOutBase.getId());
    }

    @Test
    public void addFilmNotValidName()  {

        LocalDate releaseDate = LocalDate.of(2002,10,11);
        Film film = new Film(null, null, " Ремейк итальянского ромкома 1972 года про богатую дамочку и " +
                "простого моряка, оказавшихся вместе на необитаемом острове.", releaseDate, 89); //name = Унесённые

        assertThrows(
                ValidationException.class,
                () -> {
                    filmController.create(film);
                    throw new ValidationException("Name of the film cannot be empty");
                }
        );

        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void addFilmLongDescription()  {

        LocalDate releaseDate = LocalDate.of(2001,05,10);
        Film film = new Film(null, "Большой куш", "Фрэнки Четыре Пальца должен был переправить краденый " +
                "алмаз из Англии в США своему боссу Эви, но, сделав ставку на подпольный боксерский поединок, он попал в " +
                "круговорот весьма нежелательных событий. Вокруг него и его груза разворачивается сложная интрига с участием " +
                "множества колоритных персонажей лондонского дна — русского гангстера, троих незадачливых грабителей, хитрого " +
                "боксера и угрюмого громилы грозного мафиози. Каждый норовит в одиночку сорвать большой куш", releaseDate, 104);

        assertThrows(
                ValidationException.class,
                () -> {
                    filmController.create(film);
                    throw new ValidationException("The maximum length of the description should not exceed 200 characters");
                }
        );

        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void addFilmEarlyRelease()  {

        LocalDate releaseDate = LocalDate.of(1880,02,13);   //2019-02-13
        Film film = new Film(null, "Джентльмены", "Долгожданное возвращение Гая Ричи к корням: бандиты, " +
                "нелинейное повествование, словом, всё, что так любят в британском режиссёре его самые верные фанаты.",
                releaseDate, 113);

        assertThrows(
                ValidationException.class,
                () -> {
                    filmController.create(film);
                    throw new ValidationException("The release date may not be earlier than December 28, 1895");
                }
        );

        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void addFilmPositiveDuration()  {

        LocalDate releaseDate = LocalDate.of(2009,01,29);
        Film film = new Film(null, "RocknRolla", "Опасный мир коррупции и жизни криминальных отбросов" +
                " Лондона, где недвижимость потеснила такого лидера торгового рынка как наркотики, а самыми активными " +
                "предпринимателями стали преступные группировки",releaseDate, -114);

        assertThrows(
                ValidationException.class,
                () -> {
                    filmController.create(film);
                    throw new ValidationException("The duration of the film should be positive");
                }
        );

        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void getAllFilms()  {

        LocalDate releaseDate_1 = LocalDate.of(2009,01,29);
        Film film_1 = new Film(null, "RocknRolla", "Опасный мир коррупции и жизни криминальных отбросов" +
                " Лондона, где недвижимость потеснила такого лидера торгового рынка как наркотики, а самыми активными " +
                "предпринимателями стали преступные группировки",releaseDate_1, 114);
        Film filmOutBase_1 = filmController.create(film_1);
        assertEquals(1, filmOutBase_1.getId());

        LocalDate releaseDate_2 = LocalDate.of(1998,06,23);
        Film film_2 = new Film(null, "Карты деньги два ствола", " Один из лучших фильмов Гая Ричи",
                releaseDate_2, 107);
        Film filmOutBase_2 = filmController.create(film_2);
        assertEquals(2, filmOutBase_2.getId());

        LocalDate releaseDate_3 = LocalDate.of(2019,02,13);
        Film film_3 = new Film(null, "Джентльмены", "Долгожданное возвращение Гая Ричи к корням: бандиты, " +
                "нелинейное повествование, словом, всё, что так любят в британском режиссёре его самые верные фанаты.",
                releaseDate_3, 113);
        Film filmOutBase_3 = filmController.create(film_3);
        assertEquals(3, filmOutBase_3.getId());

        LocalDate releaseDate_4 = LocalDate.of(2001,05,10);
        Film film_4 = new Film(null, "Большой куш", "Фрэнки Четыре Пальца должен был переправить краденый " +
                "алмаз из Англии в США своему боссу Эви, но, сделав ставку на подпольный боксерский поединок, он попал в " +
                "круговорот весьма нежелательных событий.", releaseDate_4, 104);
        Film filmOutBase_4 = filmController.create(film_4);
        assertEquals(4, filmOutBase_4.getId());

        LocalDate releaseDate_5 = LocalDate.of(2002,10,11);
        Film film_5 = new Film(null, "Унесенные", " Ремейк итальянского ромкома 1972 года про богатую дамочку и " +
                "простого моряка, оказавшихся вместе на необитаемом острове.", releaseDate_5, 89);
        Film filmOutBase_5 = filmController.create(film_5);
        assertEquals(5, filmOutBase_5.getId());

        assertEquals(5, filmController.findAll().size());

    }

}
