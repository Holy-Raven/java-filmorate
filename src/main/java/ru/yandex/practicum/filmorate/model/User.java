
package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private final Long id;

    private List<Long> friends = new ArrayList<>();

    @NotNull(message = "email cannot be empty")
    @NotBlank(message = "email cannot be empty")
    @Email(message = "email must contain the character @")
    private final String email;

    private final String name;

    @NotNull(message = "Your login cannot be empty or contain spaces.")
    @NotBlank(message = "Your login cannot be empty or contain spaces.")
    private final String login;

    @Past(message = "The date of birth cannot be in the future.")
    private final LocalDate birthday;

}