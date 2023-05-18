package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService implements GenreServiceInterface {

    private final GenreStorage genreStorage;

    @Override
    public List<Genre> findAll() {
        log.info("Get all list Genre");
        return new ArrayList<>(genreStorage.findAll());
    }

    @Override
    public Genre findById(long id) {

        if (genreStorage.findById(id).isPresent()) {

            Genre genre = genreStorage.findById(id).get();

            log.info("Genre id {}, name {}", genre.getId(), genre.getName());
            return genre;
        } else {
            throw new GenreNotFoundException("Genre id " +  id + " not found");
        }
    }
}
