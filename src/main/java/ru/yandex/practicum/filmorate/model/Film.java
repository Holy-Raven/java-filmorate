package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Film {

    private final Long id;

    private List<Long> likes = new ArrayList<>();

    private List<Long> genres = new ArrayList<>();

    private long mpa_id;

    @NotNull(message = "Name of the film cannot be empty")
    @NotBlank(message = "Name of the film cannot be empty")
    private final String name;

    @Size(max = 200, message = "The maximum length of the description should not exceed 200 characters")
    private final String description;

    private final LocalDate releaseDate;

    @Positive(message = "The duration of the film should be positive")
    private final Integer duration;

}


