package ru.yandex.practicum.model;


import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.enumFilmorate.StatusFriend;

import java.util.Objects;

@Getter

@Data
public class Friend {

    private final long id;

    private final StatusFriend statusFriend;

    public Friend(long id, StatusFriend statusFriend) {
        this.id = id;
        this.statusFriend = statusFriend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return id == friend.id && statusFriend == friend.statusFriend;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statusFriend);
    }
}
