
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

    @NotNull(message = "Название фильма не может быть пустым")
    @NotBlank(message = "Название фильма не может быть пустым")
    private final String name;

    @Size(max=200, message = "Максимальная длина описания не должна превышать 200 символов")
    private final String description;

    private final LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private final Integer duration;

}
