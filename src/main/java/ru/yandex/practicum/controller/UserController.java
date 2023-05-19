package ru.yandex.practicum.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int userId = 0;
    private HashMap<Integer, User> users = new HashMap<>();

    private void addUser(User user) {
        userId++;
        user.setId(userId);
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody User user) {
        log.info("Создание пользователя");
        if (users.containsKey(user.getId())) {
            log.info("Создание пользователя");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Пользователь присутствует");
        } else {
            addUser(user);
            return new ResponseEntity<User>(users.get(user.getId()), HttpStatus.OK);
        }
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь обновлен");
            return new ResponseEntity<User>(users.get(user.getId()), HttpStatus.OK);
        } else {
            log.info("Пользователь отсутствует");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Пользователь отсутствует");
        }
    }

    @GetMapping("/users")
    public List<User> getAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return  new ArrayList<User>(users.values());
    }

}
