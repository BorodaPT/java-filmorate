package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.service.GenreService;

import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@Validated
public class GenreController {
    @Autowired
    private GenreService genreService;

    private static final Logger log = LoggerFactory.getLogger(MpaController.class);

    @GetMapping("/genres/{id}")
    public Genre getUser(@PathVariable @Positive(message = "id genres не положительное число") int id) {
        log.debug("Получение genres");
        return genreService.getGenre(id);
    }

    @GetMapping("/genres")
    public List<Genre> getUsersAll() {
        log.debug("mpaService всех genres");
        return genreService.getListGenre();
    }
}
