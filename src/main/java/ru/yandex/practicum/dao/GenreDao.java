package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Genre;

import java.util.List;

public interface GenreDao {

    Genre getGenre(int id);

    List<Genre> getGenres();
}
