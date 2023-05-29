package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbControllerTest {

    private final FilmDbStorage filmStorage;

    private final JdbcTemplate jdbcTemplate;
    private static Film filmFirst;
    private static Film filmSecond;

    @BeforeEach
    public void createDataDB() {
        filmFirst = new Film(1L,"first","Test1", LocalDate.of(2020,4,1),100,new Mpa(1,"G",""));
        Set<Genre> genresF = new HashSet<>();
        genresF.add(new Genre(1,"Комедия"));
        genresF.add(new Genre(2,"Драма"));
        filmFirst.setGenres(genresF);
        filmSecond = new Film(1L,"first","Test1", LocalDate.of(2000,1,1),200,new Mpa(1,"G",""));
        Set<Genre> genresS = new HashSet<>();
        genresS.add(new Genre(1,"Комедия"));
        filmSecond.setGenres(genresS);
    }

    @AfterEach
    public void clearBase() {
        jdbcTemplate.update("delete from LIKE_FILM");
        jdbcTemplate.update("delete from GENRE_FILM");
        jdbcTemplate.update("delete from films");
    }

    @Test
    public void createFilm() {
        Film filmRes = filmStorage.create(filmFirst);
        assertEquals(filmFirst.getId(),filmRes.getId());
        assertEquals(filmFirst.getName(),filmRes.getName());
        assertEquals(filmFirst.getDescription(),filmRes.getDescription());
        assertEquals(filmFirst.getDuration(),filmRes.getDuration());
        assertEquals(filmFirst.getMpa(),filmRes.getMpa());
        assertEquals(filmFirst.getReleaseDate(),filmRes.getReleaseDate());
        assertEquals(filmFirst.getGenres(),filmRes.getGenres());
    }


    @Test
    public void getFilm() {
        Film film = filmStorage.create(filmFirst);
        Film filmRes = filmStorage.getFilm(film.getId());
        assertEquals(film.getId(),filmRes.getId());
        assertEquals(film.getName(),filmRes.getName());
        assertEquals(film.getDescription(),filmRes.getDescription());
        assertEquals(film.getDuration(),filmRes.getDuration());
        assertEquals(film.getMpa(),filmRes.getMpa());
        assertEquals(film.getReleaseDate(),filmRes.getReleaseDate());
        assertEquals(filmFirst.getGenres(),filmRes.getGenres());
    }

    @Test
    public void getFilms() {
        Film film1 = filmStorage.create(filmFirst);
        Film film2 = filmStorage.create(filmSecond);
        List<Film> filmsDef = Arrays.asList(film1,film2);
        List<Film> filmRes = filmStorage.getFilms();
        Assertions.assertArrayEquals(filmsDef.toArray(),filmRes.toArray());
    }

    @Test void getPopular() {
        User user = new User(1,"login1","login1@mail.ru","name1", LocalDate.of(2010,1,2));
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();

        Film film1 = filmStorage.create(filmFirst);
        filmStorage.create(filmSecond);
        filmStorage.setLike(film1.getId(),id,true);
        List<Film> filmRes = filmStorage.getPopular(2);
        assertEquals(film1.getId(),filmRes.get(0).getId());
    }

    @Test
    public void setLikeAdd() {
        User user = new User(1,"login1","login1@mail.ru","name1", LocalDate.of(2010,1,2));
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        Film film1 = filmStorage.create(filmFirst);
        Film film2 = filmStorage.create(filmSecond);
        filmStorage.setLike(film2.getId(), id, true);
        List<Film> filmRes = filmStorage.getPopular(2);
        assertEquals(film2.getId(),filmRes.get(0).getId());
        filmStorage.setLike(film2.getId(), id, false);
        filmRes = filmStorage.getPopular(2);
        assertEquals(film1.getId(),filmRes.get(0).getId());
    }

    @Test
    public void update() {
        Film film1 = filmStorage.create(filmFirst);
        film1.setDescription(filmSecond.getDescription());
        filmStorage.update(film1);
        Film filmRes = filmStorage.getFilm(film1.getId());
        assertEquals(filmFirst,filmRes);
    }



}
