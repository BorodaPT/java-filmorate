package ru.yandex.practicum.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {

    private long userId;
    private HashMap<Long, User> users;

    public InMemoryUserStorage() {
        userId = 0;
        users = new HashMap<>();
    }

    @Override
    public User getUser(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ExceptionNotFound("selectUser","Пользователь не найден");
        }
    }

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            throw new ExceptionNotFound("createUser","Пользователь уже зарегестрирован");
        } else {
            userId++;
            user.setId(userId);
            users.put(user.getId(), user);
            return users.get(user.getId());
        }
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return users.get(user.getId());
        } else {
            throw new ExceptionNotFound("updateUser","Пользователь для обновления отсутствует");
        }
    }

    @Override
    public void delete(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new ExceptionNotFound("deleteFilm","Фильм для удаления отсутствует");
        }
    }

    @Override
    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private void addFriend(long idUserMain, long idUserFriend) {
        if (users.containsKey(idUserMain)) {
            User user = users.get(idUserMain);
            Set<Long> friends = user.getFriends();
            friends.add(idUserFriend);
            user.setFriends(friends);
            users.put(idUserMain,user);
        } else {
            throw new ExceptionNotFound("addFriend", "User not found");
        }
    }

    @Override
    public User addFriendPost(long idUserMain, long idUserFriend) {
        addFriend(idUserMain, idUserFriend);
        addFriend(idUserFriend, idUserMain);
        return users.get(idUserMain);
    }

    private void deleteFriend(long idUserMain, long idUserFriend) {
        if (users.containsKey(idUserMain)) {
            User user = users.get(idUserMain);
            Set<Long> friends = user.getFriends();
            if (friends.contains(idUserFriend)) {
                friends.remove(idUserFriend);
                user.setFriends(friends);
                users.put(idUserMain, user);
            }
        } else {
            throw new ExceptionNotFound("addFriend","User not found");
        }
    }

    @Override
    public User deleteFriendPost(long idUserMain, long idUserFriend) {
        deleteFriend(idUserMain, idUserFriend);
        deleteFriend(idUserFriend, idUserMain);
        return users.get(idUserMain);
    }

    @Override
    public ArrayList<User> getFriend(long id) {
        ArrayList<User> resultList = new ArrayList<>();
        Set<Long> friends = users.get(id).getFriends();
        if (!friends.isEmpty()) {
            for (Long i : friends) {
                resultList.add(users.get(i));
            }
        }
        return resultList;
    }

    @Override
    public ArrayList<User> getCommonFriend(long idUserMain, long idUserFriend) {
        ArrayList<User> resultList = new ArrayList<>();
        if (users.containsKey(idUserFriend) && users.containsKey(idUserMain)) {
            Set<Long> main = users.get(idUserMain).getFriends();
            Set<Long> friend = users.get(idUserFriend).getFriends();
            Set<Long> intersection = new HashSet<>(main);
            intersection.retainAll(friend);
            if (!intersection.isEmpty()) {
                for (Long i : intersection) {
                    resultList.add(users.get(i));
                }
            }
        }
        return resultList;
    }
}
