package ru.yandex.practicum.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Component("userDbStorage")
public class UserDbStorage implements UserStorage{

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(long id) {
        log.info("Получение пользователя {}", id);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getLong("id"), userRows.getString("LOGIN"));
            return new User(
                    userRows.getInt("id"),
                    userRows.getString("LOGIN"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getDate("BIRTHDAY").toLocalDate()
            );
        } else {
            log.info("Пользователь не найден.");
            throw new ExceptionNotFound("selectUser","Пользователь не найден");
        }
    }

    @Override
    public List<User> getUsers() {
        log.info("Получение списка пользователя");
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("LOGIN"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    @Override
    public User create(User user) {
        log.info("Создание пользователя");
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        log.info("Обновление пользователя");
        String sqlQuery = "update users set " +
                "login = ?, name = ?, email = ?, birthday = ? " +
                "where id = ?";
        if (jdbcTemplate.update(sqlQuery
                , user.getLogin()
                , user.getName()
                , user.getEmail()
                , user.getBirthday()
                , user.getId()) > 0) {
            return user;
        } else {
            throw new ExceptionNotFound("updateUser","Пользователь для обновления отсутствует");
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление пользователя");
        String sqlQuery = "delete from users where id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0) {
            throw new ExceptionNotFound("deleteuser","Не удалось удалить пользователя");
        }
    }

    @Override
    public List<User> getFriend(long id) {
        log.info("Получение списка друзей");
        String sql = "SELECT * \n" +
                "FROM USERS u \n" +
                "JOIN (SELECT FRIEND_ID  FROM FRIENDS f WHERE USER_ID = ?) allFriend ON u.ID = allFriend.FRIEND_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs),id);
    }

    @Override
    public List<User> getCommonFriend(long idUserMain, long idUserFriend) {
        log.info("Получение списка друзей");
        String sql = "SELECT * \n" +
                "FROM USERS u \n" +
                "JOIN (\n" +
                "SELECT DISTINCT us1.FRIEND_ID AS fId FROM FRIENDS us1 \n" +
                "JOIN (SELECT DISTINCT FRIEND_ID FROM FRIENDS f WHERE USER_ID = ?) us2 ON US1.FRIEND_ID = us2.FRIEND_ID\n" +
                "WHERE USER_ID = ?) allFriend ON u.ID = allFriend.fId";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs),idUserFriend,idUserMain);
    }

    @Override
    public User addFriendPost(long idUserMain, long idUserFriend) {
        log.info("Отправка заявки в друзья");
        String sqlQuery;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDS f WHERE USER_ID  = ? AND FRIEND_ID = ?", idUserFriend,idUserMain);
        boolean checkFriedn = userRows.next();
        if (checkFriedn) {
            sqlQuery = "UPDATE FRIENDS SET REQUEST_STATUS_ID = 2 WHERE USER_ID  = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    idUserFriend,
                    idUserMain);

            sqlQuery = "INSERT INTO FRIENDS (USER_ID,FRIEND_ID,REQUEST_STATUS_ID) " +
                    "VALUES (?,?,2)";
        } else {
            sqlQuery = "INSERT INTO FRIENDS (USER_ID,FRIEND_ID,REQUEST_STATUS_ID) " +
                    "VALUES (?,?,1)";
        }
        if (
        jdbcTemplate.update(sqlQuery,
                    idUserMain,
                    idUserFriend) > 0) {
            //получение списка друзей
            User user = getUser(idUserMain);
            user.setFriends(new HashSet<>(getUserFriends(idUserMain)));
            return user;
        } else {
            throw new ExceptionNotFound("addFriend","не удалось удалить пользователя");
        }
    }


    private List<Long> getUserFriends(long id) {
        List<Long> friends = new ArrayList<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("SELECT friend_id FROM FRIENDS f WHERE USER_ID  = ?", id);
        while (sqlRows.next()) {
            friends.add(sqlRows.getLong("friend_id"));
        }
        return friends;
    }

    @Override
    public User deleteFriendPost(long idUserMain, long idUserFriend) {
        log.info("Отправка заявки в друзья");
        String sqlQuery = "DELETE FROM FRIENDS WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
        if (jdbcTemplate.update(sqlQuery,
                idUserMain,
                idUserFriend,
                idUserFriend,
                idUserMain) > 0) {
            User user = getUser(idUserMain);
            user.setFriends(new HashSet<>(getUserFriends(idUserMain)));
            return user;
        } else {
            throw new ExceptionNotFound("deleteFriend","не удалось удалить пользователя");
        }
    }
}
