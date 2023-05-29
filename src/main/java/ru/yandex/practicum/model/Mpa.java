package ru.yandex.practicum.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
@Setter

@RequiredArgsConstructor
@Data
public class Mpa {

    @NotNull
    private int id;

    @Nullable
    private String name;

    @Nullable
    private String description;

    public Mpa(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
