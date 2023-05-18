package ru.yandex.practicum.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString

@Data
public class User {
    int id;
    @Email(message = "Неверный формат электронной почты")
    private String email;
    @NotEmpty
    @NotBlank(message = "Логин не может быть пустым или состоять только из пробелов")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть больше текущей")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
