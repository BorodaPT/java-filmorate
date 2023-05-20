package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ExceptionDataRequest;
import ru.yandex.practicum.model.Film;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int filmId = 0;
    private HashMap<Integer, Film> films = new HashMap<>();

    private void addFilm(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(film.getId(), film);
    }

    @PostMapping("/films")
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        log.info("Создание фильма");
        if (films.containsKey(film.getId())) {
            log.info("Фильм уже есть");
            throw new ExceptionDataRequest("createFilm","Фильм уже зарегестрирован");
        } else {
            addFilm(film);
            return new ResponseEntity<Film>(films.get(film.getId()), HttpStatus.OK);
        }
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");
        if (films.containsKey(film.getId())) {
            log.info("Фильм обновлен");
            films.put(film.getId(), film);
            return new ResponseEntity<Film>(films.get(film.getId()), HttpStatus.OK);
        } else {
            log.info("Фильм отсутствует");
            throw new ExceptionDataRequest("createFilm","Фильм для обновления отсутствует");
        }
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<Film>(films.values());
    }

}
