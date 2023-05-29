package ru.yandex.practicum.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaImpl implements MpaDao {

    private final Logger log = LoggerFactory.getLogger(MpaImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public MpaImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpa(int id) {
        log.info("Получение MPA {}", id);
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id = ?", id);
        if (mpaRows.next()) {
            log.info("Найден MPA: {} {}", mpaRows.getLong("id"), mpaRows.getString("name"));
            return new Mpa(
                    mpaRows.getInt("ID"),
                    mpaRows.getString("NAME"),
                    mpaRows.getString("DESCRIPTION")
            );
        } else {
            log.info("MPA не найден.");
            throw new ExceptionNotFound("selectMPA","MPA не найден");
        }
    }

    @Override
    public List<Mpa> getListMpa() {
        log.info("Получение списка жанров");
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs));
    }

    private Mpa makeMPA(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION")
                );
    }
}
