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
    void findById() {
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
    void findAll() {

        final List<Genre> genreList = genreStorage.findAll();

        assertThat(genreList.size()).isEqualTo(6);

        Genre genre_id_1 = genreStorage.findById(1L).get();
        Genre genre_id_2 = genreStorage.findById(2L).get();
        Genre genre_id_3 = genreStorage.findById(3L).get();
        Genre genre_id_4 = genreStorage.findById(4L).get();
        Genre genre_id_5 = genreStorage.findById(5L).get();
        Genre genre_id_6 = genreStorage.findById(6L).get();

        assertThat(genreList.get(0)).isEqualTo(genre_id_1);
        assertThat(genreList.get(1)).isEqualTo(genre_id_2);
        assertThat(genreList.get(2)).isEqualTo(genre_id_3);
        assertThat(genreList.get(3)).isEqualTo(genre_id_4);
        assertThat(genreList.get(4)).isEqualTo(genre_id_5);
        assertThat(genreList.get(5)).isEqualTo(genre_id_6);

    }

}
