package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Mpa {

    private Long id;

    private String name;

    public Mpa(Long id) {
        this.id = id;
    }
}
