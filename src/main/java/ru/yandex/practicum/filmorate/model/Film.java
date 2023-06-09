package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private final Long id;

    @NotNull(message = "Name of the film cannot be empty")
    @NotBlank(message = "Name of the film cannot be empty")
    private final String name;

    @Size(max = 200, message = "The maximum length of the description should not exceed 200 characters")
    private final String description;

    private final LocalDate releaseDate;

    @NotNull
    @Positive(message = "The duration of the film should be positive")
    private final Integer duration;

    private final Mpa mpa;

    private Set<Long> likes = new HashSet<>();

    private Set<Genre> genres = new HashSet<>();

}


