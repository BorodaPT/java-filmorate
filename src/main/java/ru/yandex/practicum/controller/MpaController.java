package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.service.MpaService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
public class MpaController {

    @Autowired
    private MpaService mpaService;

    private static final Logger log = LoggerFactory.getLogger(MpaController.class);

    @GetMapping("/mpa/{id}")
    public Mpa getUser(@PathVariable @Positive(message = "id MPA не положительное число") int id) {
        log.debug("Получение MPA");
        return mpaService.getMpa(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getUsersAll() {
        log.debug("mpaService всех MPA");
        return mpaService.getListMpa();
    }


}
