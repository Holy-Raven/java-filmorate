
package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private final Integer id;

    @NotNull(message = "Name of the film cannot be empty")
    @NotBlank(message = "Name of the film cannot be empty")
    private final String name;

    @Size(max=200, message = "The maximum length of the description should not exceed 200 characters")
    private final String description;

    private final LocalDate releaseDate;

    @Positive(message = "The duration of the film should be positive")
    private final Integer duration;

}
