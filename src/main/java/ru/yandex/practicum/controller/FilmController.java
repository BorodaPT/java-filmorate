package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@Validated
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private FilmService filmService;

    @PostMapping("/films")
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        log.info("Создание фильма");
        return new ResponseEntity<Film>(filmService.create(film), HttpStatus.OK);
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");
        return new ResponseEntity<Film>(filmService.update(film), HttpStatus.OK);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public ResponseEntity<Film> updateFilmLike(@PathVariable @Positive(message = "id фильма не положительное число") long id,@PathVariable long userId) {
        log.info("Обновление фильма");
        return new ResponseEntity<Film>(filmService.updateLike(id,userId, true), HttpStatus.OK);
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<Film> selectFilm(@PathVariable @Positive(message = "id фильма не положительное число") long id) {
        log.info("Обновление фильма");
        return new ResponseEntity<Film>(filmService.getFilm(id), HttpStatus.OK);
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularAll(@RequestParam(required = false,defaultValue = "10") Long count) {
        log.info("Получение популярных фильмов");
        return filmService.getPopular(count);
    }

    @DeleteMapping("/films/{id}")
    public ResponseEntity deleteFilm(@PathVariable @Positive(message = "id фильма не положительное число") long id) {
        log.info("Обновление фильма");
        filmService.deleteFilm(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<Film> updateFilmLikeDel(@PathVariable @Positive(message = "id фильма не положительное число") long id,@PathVariable long userId) {
        log.info("Обновление фильма");
        return new ResponseEntity<Film>(filmService.updateLike(id,userId, false), HttpStatus.OK);
    }

}
