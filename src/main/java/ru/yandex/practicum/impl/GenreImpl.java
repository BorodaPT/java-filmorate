package ru.yandex.practicum.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.model.Genre;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreImpl implements GenreDao {

    private final Logger log = LoggerFactory.getLogger(GenreImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public GenreImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int id) {
        log.info("Получение жанра {}", id);
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE id = ?", id);
        if (genreRows.next()) {
            log.info("Найден жанр: {} {}", genreRows.getLong("id"), genreRows.getString("name"));
            return new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            );
        } else {
            log.info("Жанр не найден.");
            throw new ExceptionNotFound("selectGenre","Жанр не найден");
        }
    }

    @Override
    public List<Genre> getGenres() {
        log.info("Получение списка жанров");
        String sql = "SELECT * FROM genre ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"),
                         rs.getString("name"));
    }

}
