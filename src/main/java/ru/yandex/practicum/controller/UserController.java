package ru.yandex.practicum.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int userId = 0;
    private HashMap<String, User> users = new HashMap<>();

    private void addUser(User user) {
        userId++;
        user.setId(userId);
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        users.put(user.getLogin(), user);
    }

    private boolean checkUser(User user) {
        return users.containsKey(user.getLogin());
    }

    @PostMapping("/users")
    public String createUser(@Valid @RequestBody User user) {
        log.info("Создание пользователя");
        if (checkUser(user)) {
            return "Данный логин уже зарегестрирован";
        } else {
            addUser(user);
            return "Пользователь добавлен";
        }
    }

    @PutMapping("/users")
    public String updateUser(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");
        if (checkUser(user)) {
            users.put(user.getLogin(), user);
            return "Пользователь обновлен";
        } else {
            return "Пользователя не существует";
        }
    }

    @GetMapping("/users/list")
    public List<User> getAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return  new ArrayList<User>(users.values());
    }

}
