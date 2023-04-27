package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friendship {

    private final Long first_user_id;
    private final Long second_user_id;

}
