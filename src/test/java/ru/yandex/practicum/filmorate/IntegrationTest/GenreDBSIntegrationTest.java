package ru.yandex.practicum.filmorate.IntegrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class GenreDBSIntegrationTest {

    final GenreStorage genreStorage;

    @Test
    void testFindById() {
        final long id = 4L;

        final Optional<Genre> genreOptional = genreStorage.findById(id);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre)
                                .hasFieldOrPropertyWithValue("id", id)
                                .hasFieldOrPropertyWithValue("name", "Триллер"));
    }

    @Test
    void testFindAll() {

        final List<Genre> genreList = genreStorage.findAll();

        assertThat(genreList.size()).isEqualTo(6);

        Genre genreId1 = genreStorage.findById(1L).get();
        Genre genreId2 = genreStorage.findById(2L).get();
        Genre genreId3 = genreStorage.findById(3L).get();
        Genre genreId4 = genreStorage.findById(4L).get();
        Genre genreId5 = genreStorage.findById(5L).get();
        Genre genreId6 = genreStorage.findById(6L).get();

        assertThat(genreList.get(0)).isEqualTo(genreId1);
        assertThat(genreList.get(1)).isEqualTo(genreId2);
        assertThat(genreList.get(2)).isEqualTo(genreId3);
        assertThat(genreList.get(3)).isEqualTo(genreId4);
        assertThat(genreList.get(4)).isEqualTo(genreId5);
        assertThat(genreList.get(5)).isEqualTo(genreId6);

    }

}
