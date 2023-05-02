package ru.yandex.practicum.filmorate.IntegrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class MpaDBSIntegrationTest {

    final MpaStorage mpaStorage;
    @Test
    void findById() {

        final long id = 5L;

        final Optional<Mpa> mpaOptional = mpaStorage.findById(id);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id",id)
                                .hasFieldOrPropertyWithValue("name", "NC-17"));
    }
    @Test
    void findAll() {

        final List<Mpa> mpaList = mpaStorage.findAll();

        assertThat(mpaList.size()).isEqualTo(5);

        Mpa mpa_id_1 = mpaStorage.findById(1L).get();
        Mpa mpa_id_2 = mpaStorage.findById(2L).get();
        Mpa mpa_id_3 = mpaStorage.findById(3L).get();
        Mpa mpa_id_4 = mpaStorage.findById(4L).get();
        Mpa mpa_id_5 = mpaStorage.findById(5L).get();

        assertThat(mpaList.get(0)).isEqualTo(mpa_id_1);
        assertThat(mpaList.get(1)).isEqualTo(mpa_id_2);
        assertThat(mpaList.get(2)).isEqualTo(mpa_id_3);
        assertThat(mpaList.get(3)).isEqualTo(mpa_id_4);
        assertThat(mpaList.get(4)).isEqualTo(mpa_id_5);

    }

}
