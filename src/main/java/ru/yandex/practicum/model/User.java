package ru.yandex.practicum.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

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

    public User(long id, String login, String email, @Nullable String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("login", login);
        values.put("name", name);
        values.put("email", email);
        values.put("birthday", birthday);
        return values;
    }
}
