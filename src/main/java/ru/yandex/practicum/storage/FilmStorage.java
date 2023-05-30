package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;

import java.util.List;


public interface FilmStorage {

    Film getFilm(long id);

    Film create(Film film);

    Film update(Film film);

    void delete(Long id);

    Film setLike(Long idFilm, Long idUser, boolean isInstalled);

    List<Film> getFilms();

    List<Film> getPopular(long count);

}
