package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int filmId = 0;
    private HashMap<String, Film> films = new HashMap<>();

    private void addFilm(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(film.getName(), film);
    }

    private boolean checkFilm(Film film) {
        return films.containsKey(film.getName());
    }

    @PostMapping("/films")
    public String createUser(@Valid @RequestBody Film film) {
        log.info("Создание фильма");
        if (checkFilm(film)) {
            return "Данный фильм уже добавлен";
        } else {
            addFilm(film);
            return "Фильм добавлен";
        }
    }

    @PutMapping("/films/name")
    public String updateUser(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");
        if (checkFilm(film)) {
            films.put(film.getName(), film);
            return "Филь обновлен";
        } else {
            return "Фильма не существует";
        }
    }

    @GetMapping("/films/list")
    public List<Film> getAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<Film>(films.values());
    }

}
