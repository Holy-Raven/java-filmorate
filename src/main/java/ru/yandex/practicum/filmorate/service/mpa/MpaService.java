package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService implements MpaServiceInterface {

    private final MpaStorage mpaStorage;

    @Override
    public List<Mpa> findAll() {
        log.info("All list MPA");
        return new ArrayList<>(mpaStorage.findAll());
    }

    @Override
    public Mpa findById(long id) {

        if (mpaStorage.findById(id).isPresent()) {

            Mpa mpa = mpaStorage.findById(id).get();

            log.info("Mpa id {}, name {}", mpa.getId(), mpa.getName());

            return mpa;
        } else {
            throw new MpaNotFoundException("Mpa id " +  id + " not found");
        }
    }

}