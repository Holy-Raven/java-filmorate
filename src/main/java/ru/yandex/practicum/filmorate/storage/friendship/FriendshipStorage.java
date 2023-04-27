package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipStorage {

    List<Long> findAllById(long id);
    void add(Friendship friendship);
    void put(Friendship friendship);

    Optional<Friendship> findById(Friendship friendship);

    void deleteById(Friendship friendship);

    boolean status(Friendship friendship);

    boolean isExist(Friendship friendship);
}