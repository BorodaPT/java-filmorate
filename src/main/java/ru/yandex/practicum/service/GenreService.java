package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.model.Genre;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreDao genreDao;

    public Genre getGenre(int id) {
        return genreDao.getGenre(id);
    }

    public List<Genre> getListGenre() {
        return genreDao.getGenres();
    }
}
