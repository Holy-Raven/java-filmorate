package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    public final Map<Long, User> users = new HashMap<>();

    @Override
    public Map<Long, User> allUsers() {
        return users;
    }

    @Override
    public User add(User user) {
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public User put(User user) {
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public User del(User user) {
        users.remove(user.getId());
        return user;
    }
}
