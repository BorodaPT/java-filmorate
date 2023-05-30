package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserDbControllerTest {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userDbStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static User userFirst;

    private static User userSecond;

    private static User userThird;

    @BeforeEach
    public void createData() {
        userFirst = new User(1,"login1","login1@mail.ru","name1", LocalDate.of(2010,1,2));
        userSecond = new User(2,"login2","login2@mail.ru","name2", LocalDate.of(1990,1,2));
        userThird = new User(3,"login3","login3@mail.ru","name3", LocalDate.of(1980,2,2));
    }

    @AfterEach
    public void clearBase() {
        jdbcTemplate.update("delete from users");
    }

    @Test
    public void getUser() {
        User user = userDbStorage.create(userFirst);
        User userRes = userDbStorage.getUser(user.getId());
        assertEquals(user,userRes);
    }

    @Test
    public void getUsers() {
        User user = userDbStorage.create(userFirst);
        User user2 = userDbStorage.create(userSecond);
        List<User> listDef =  Arrays.asList(user,user2);
        List<User> listRes = userDbStorage.getUsers();
        Assertions.assertArrayEquals(listDef.toArray(),listRes.toArray());
    }

    @Test
    public void create() {
        User user = userDbStorage.create(userFirst);
        User userRes = userDbStorage.getUser(user.getId());
        assertEquals(user,userRes);
    }

    @Test
    public void update() {
        User user = userDbStorage.create(userFirst);
        user.setName("editName");
        userDbStorage.update(user);
        User userRes = userDbStorage.getUser(user.getId());
        assertEquals(user,userRes);
    }

    @Test
    public void getFriend() {
        User user = userDbStorage.create(userFirst);
        User user2 = userDbStorage.create(userSecond);
        userDbStorage.addFriendPost(user.getId(),user2.getId());
        List<User> friedDef = Arrays.asList(user2);
        List<User> friedRes = userDbStorage.getFriend(user.getId());
        Assertions.assertArrayEquals(friedDef.toArray(),friedRes.toArray());
    }

    @Test
    public void getCommonFriend() {
        User user = userDbStorage.create(userFirst);
        User user2 = userDbStorage.create(userSecond);
        User user3 = userDbStorage.create(userThird);
        userDbStorage.addFriendPost(user.getId(),user3.getId());
        userDbStorage.addFriendPost(user2.getId(),user3.getId());
        List<User> friedDef = Arrays.asList(user3);
        List<User> friedRes = userDbStorage.getCommonFriend(user.getId(),user2.getId());
        Assertions.assertArrayEquals(friedDef.toArray(),friedRes.toArray());
    }

    @Test
    public void addFriendPost() {
        User user = userDbStorage.create(userFirst);
        User user2 = userDbStorage.create(userSecond);
        userDbStorage.addFriendPost(user.getId(),user2.getId());
        List<User> friedDef = Arrays.asList(user2);
        List<User> friedRes = userDbStorage.getFriend(user.getId());
        Assertions.assertArrayEquals(friedDef.toArray(),friedRes.toArray());
    }

    @Test
    public void deleteFriendPost() {
        User user = userDbStorage.create(userFirst);
        User user2 = userDbStorage.create(userSecond);
        User user3 = userDbStorage.create(userThird);
        userDbStorage.addFriendPost(user.getId(),user2.getId());
        userDbStorage.addFriendPost(user.getId(),user3.getId());
        List<User> friedDef = Arrays.asList(user3);
        userDbStorage.deleteFriendPost(user.getId(),user2.getId());
        List<User> friedRes = userDbStorage.getFriend(user.getId());
        Assertions.assertArrayEquals(friedDef.toArray(),friedRes.toArray());
    }

}
