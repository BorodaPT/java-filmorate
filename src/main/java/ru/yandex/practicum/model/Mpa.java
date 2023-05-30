package ru.yandex.practicum.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;


@RequiredArgsConstructor
@Data
public class Mpa {

    private int id;

    private String name;

    @Nullable
    private String description;

    public Mpa(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
