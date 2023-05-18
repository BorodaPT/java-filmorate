package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.validation.MinDateFilms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Data
public class Film {
    int id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Превышена максимальная длина описания(200)") //validation.name.size.too_long
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd")
    @MinDateFilms(message = "Дата фильма не может быть меньше 28.12.1895")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительной")
    private int duration;

}
