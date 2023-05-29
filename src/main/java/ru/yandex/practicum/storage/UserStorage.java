package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;

import java.util.List;

public interface UserStorage {
    User getUser(long id);

    User create(User user);

    User update(User user);

    void delete(Long id);

    List<User> getUsers();

    List<User> getFriend(long id);

    List<User> getCommonFriend(long idUserMain, long idUserFriend);

    User addFriendPost(long idUserMain, long idUserFriend);

    User deleteFriendPost(long idUserMain, long idUserFriend);
}
