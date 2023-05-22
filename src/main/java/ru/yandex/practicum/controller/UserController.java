package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;

@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Создание пользователя");
        return new ResponseEntity<User>(userService.create(user), HttpStatus.OK);
    }

    //PUT
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");
        return new ResponseEntity<User>(userService.update(user), HttpStatus.OK);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable @Positive(message = "id пользователя не положительное число") long id,@PathVariable long friendId) {
        log.info("Добавление друга");
        return new ResponseEntity<User>(userService.addFriendPost(id,friendId), HttpStatus.OK);
    }

    //GET
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable @Positive(message = "id пользователя не положительное число") long id) {
        log.debug("Получение пользователя");
        return userService.getUser(id);
    }

    @GetMapping("/users")
    public ArrayList<User> getUsersAll() {
        log.debug("Получение всех пользователей");
        return userService.getUsers();
    }

    @GetMapping("/users/{id}/friends")
    public ArrayList<User> getFriendAll(@PathVariable @Positive(message = "id пользователя не положительное число") long id) {
        log.debug("Получение списка друзей");
        return userService.getFriend(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public ArrayList<User> getAll(@PathVariable @Positive(message = "id пользователя не положительное число") long id,
                                  @PathVariable @Positive(message = "id пользователя не положительное число") long otherId) {
        log.debug("Получение обзих друзей");
        return userService.getCommonFriend(id, otherId);
    }

    //DELETE
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable @Positive(message = "id фильма не положительное число") long id,
                                             @PathVariable @Positive(message = "id фильма не положительное число") long friendId) {
        log.info("Удаление друга");
        return new ResponseEntity<User>(userService.deleteFriendPost(id,friendId), HttpStatus.OK);
    }


}
