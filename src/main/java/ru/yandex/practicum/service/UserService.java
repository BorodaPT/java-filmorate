package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;
import java.util.List;


@Service
public class UserService {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    public User getUser(long id) {
        return userStorage.getUser(id);
    }

    public User create(User user) {
        if (user.getName().equals("")) user.setName(user.getLogin());
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getName().equals("")) user.setName(user.getLogin());
        return userStorage.update(user);
    }

    public void delete(Long id) {
       userStorage.delete(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addFriendPost(long idUserMain, long idUserFriend) {
        if (idUserFriend < 1) throw new ExceptionNotFound("Добавление друга","Не найден " + idUserFriend);
        return userStorage.addFriendPost(idUserMain,idUserFriend);
    }

    public User deleteFriendPost(long idUserMain, long idUserFriend) {
        return userStorage.deleteFriendPost(idUserMain,idUserFriend);
    }

    public List<User> getFriend(long id) {
        return userStorage.getFriend(id);
    }

    public List<User> getCommonFriend(long idUserMain, long idUserFriend) {
        return userStorage.getCommonFriend(idUserMain,idUserFriend);
    }
}
