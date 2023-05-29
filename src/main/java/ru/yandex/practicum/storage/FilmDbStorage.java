package ru.yandex.practicum.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilm(long id) {
        log.info("Получение фильма {}", id);
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.*, m.name AS NAME_MPA FROM films f " +
                "JOIN MPA m ON m.id = f.mpa_id " +
                " where f.id = ?", id);
        if (filmRows.next()) {
            log.info("Найден фильм: {} {}", filmRows.getLong("id"), filmRows.getString("name"));
            Film film = new Film(
                    filmRows.getLong("ID"),
                    filmRows.getString("NAME"),
                    filmRows.getString("DESCRIPTION"),
                    filmRows.getDate("RELEASEDATE").toLocalDate(),
                    filmRows.getInt("DURATION"),
                    new Mpa(filmRows.getInt("MPA_ID"),filmRows.getString("NAME_MPA"),"")
            );
            film.setGenres(getGenreFilm(film.getId()));
            return film;
        } else {
            log.info("Фильм не найден.");
            throw new ExceptionNotFound("selectFilm","Фильм не найден");
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
         Film film = new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("mpa_id"),rs.getString("mpa_name"),"")
         );
         film.setGenres(getGenreFilm(film.getId()));
         return film;
    }

    private Set<Genre> getGenreFilm(long idFilm) {
        Set<Genre> genres = new HashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                                            "SELECT g.* FROM GENRE g \n" +
                                                "JOIN GENRE_FILM gf ON g.ID = gf.GENRE_ID \n" +
                                                "WHERE gf.FILM_ID  = ? \n" +
                                                "ORDER BY g.id", idFilm);
        while (genreRows.next()) {
            genres.add(new Genre(genreRows.getInt("id"),genreRows.getString("name")));
        }
        return genres;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Получение списка фильмов");
        String sql = "SELECT f.*,m.name as mpa_name FROM films f " +
                "JOIN MPA m ON m.id = f.mpa_id " +
                "ORDER BY name";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public List<Film> getPopular(long count) {
        log.info("Получение списка фильмов");
        String sql = "SELECT F.*,m.name as mpa_name,likeF FROM FILMS F\n" +
                "\tLEFT JOIN (\n" +
                "\t\tSELECT FILM_ID, COUNT(*) AS likeF FROM LIKE_FILM\n" +
                "\t\tGROUP BY FILM_ID\n" +
                ") lf ON f.id = lf.FILM_ID\n" +
                "JOIN MPA m ON m.id = f.mpa_id\n" +
                "ORDER BY likeF DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs),count);
    }

    @Override
    public Film create(Film film) {
        log.info("Создание фильма");
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
        film.setId(id);
        createFilmGenre(film);
        return film;
    }

    private void createFilmGenre(Film film) {
        log.info("Заполнение жанров");
        String sqlQuery = "DELETE FROM GENRE_FILM WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        if (film.getGenres() != null) {
            Set<Genre> genres = film.getGenres();
            for (Genre genre : genres) {
                sqlQuery = "MERGE INTO GENRE_FILM Key(film_id,genre_id) VALUES (?,?)";
                if (jdbcTemplate.update(sqlQuery,
                        film.getId(),
                        genre.getId()) == 0) {
                    throw new ExceptionNotFound("addGenre", "не удалось установить жанр");
                }
            }
        }
    }

    @Override
    public Film update(Film film) {
        log.info("Обновление пользователя");
        String sqlQuery = "update films set " +
                "name = ?, description = ?, duration = ?, releaseDate = ?, mpa_id = ? " +
                "where id = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId()) > 0) {
            createFilmGenre(film);
            return film;
        } else {
            throw new ExceptionNotFound("updateFilm","Фильм для обновления отсутствует");
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление фильма");
        String sqlQuery = "delete from films where id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0) {
            throw new ExceptionNotFound("deleteFilm","Не удалось удалить фильм");
        }
    }

    @Override
    public Film setLike(Long idFilm, Long idUser, boolean isInstalled) {
        String sqlQuery;
        if (isInstalled) {
            sqlQuery = "INSERT INTO LIKE_FILM (film_id,user_id) VALUES (?,?)";
        } else {
            sqlQuery = "DELETE FROM LIKE_FILM WHERE film_id = ? AND user_id = ?";
        }
        if (jdbcTemplate.update(sqlQuery,
                                idFilm,
                                idUser) > 0) {
            //получение списка друзей
            Film film = getFilm(idFilm);
            film.setLike(new HashSet<>(getFilmLike(idFilm)));
            return film;
        } else {
            throw new ExceptionNotFound("addLike","не удалось установить like");
        }
    }

    private List<Long> getFilmLike(Long idFilm) {
        List<Long> likes = new ArrayList<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM like_film WHERE film_id  = ?", idFilm);
        while (sqlRows.next()) {
            likes.add(sqlRows.getLong("user_id"));
        }
        return likes;
    }

}
