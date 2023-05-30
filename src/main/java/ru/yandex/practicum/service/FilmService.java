package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;

    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film updateLike(long idFilm,long idUser, boolean isInstalled) {
        if (idUser < 1) throw new ExceptionNotFound("Обновление like","Не найден " + idUser);
        return filmStorage.setLike(idFilm, idUser, isInstalled);
    }

    public void deleteFilm(long id) {
        filmStorage.getFilm(id);
    }

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getFilms());
    }

    public List<Film> getPopular(long count) {
        return filmStorage.getPopular(count);
    }
}
