
package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {

    private final Integer id;

    @NotNull(message = "email не может быть пустым")
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "email должен содержать @")
    private final String email;

    private final String name;

    @NotNull(message = "Your login cannot be empty or contain spaces.")
    @NotBlank(message = "Your login cannot be empty or contain spaces.")
    private final String login;

    @Past(message = "The date of birth cannot be in the future.")
    private final LocalDate birthday;

}