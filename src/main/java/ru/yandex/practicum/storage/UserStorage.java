package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;

import java.util.ArrayList;

public interface UserStorage {
    User getUser(long id);

    User create(User user);

    User update(User user);

    void delete(Long id);

    ArrayList<User> getUsers();

    ArrayList<User> getFriend(long id);

    ArrayList<User> getCommonFriend(long idUserMain, long idUserFriend);

    User addFriendPost(long idUserMain, long idUserFriend);

    User deleteFriendPost(long idUserMain, long idUserFriend);
}
