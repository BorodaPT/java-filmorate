package ru.yandex.practicum.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString

@Data
public class User {
    long id;
    @Email(message = "Неверный формат электронной почты")
    private String email;
    @NotEmpty(message = "Логин не может быть пустым или состоять только из пробелов")
    @NotBlank(message = "Логин не может быть пустым или состоять только из пробелов")
    private String login;
    @Nullable
    private String name;
    @Past(message = "Дата рождения не может быть больше текущей")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @Nullable
    private Set<Long> friends;

    public User() {
        friends = new HashSet<>();
    }
}
