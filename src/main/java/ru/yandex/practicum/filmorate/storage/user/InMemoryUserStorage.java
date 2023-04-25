//package ru.yandex.practicum.filmorate.storage.user;
//
//import org.springframework.stereotype.Repository;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.util.*;
//
//@Repository("InMemoryUserStorage")
//public class InMemoryUserStorage implements UserStorage {
//
//    public final Map<Long, User> users = new HashMap<>();
//
//    @Override
//    public Map<Long, User> allUsers() {
//        return users;
//    }
//
//    @Override
//    public User add(User user) {
//        users.put(user.getId(),user);
//        return user;
//    }
//
//    @Override
//    public User put(User user) {
//        users.put(user.getId(),user);
//        return user;
//    }
//
//    @Override
//    public User del(User user) {
//        users.remove(user.getId());
//        return user;
//    }
//
//    @Override
//    public Optional<User> findUserById(Long userId) {
//        return allUsers().get(userId);
//    }
//
//    @Override
//    public User addFriends(Long user1, Long user2) {
//        findUserById(user1).getFriends().add(user2);
//        findUserById(user2).getFriends().add(user2);
//        return allUsers().get(user1);
//    }
//
//    @Override
//    public User delFriends(Long user1, Long user2) {
//        findUserById(user1).getFriends().remove(user2);
//        findUserById(user2).getFriends().remove(user1);
//        return allUsers().get(user1);
//    }
//
//    @Override
//    public List<User> friendsList(Long user) {
//
//        List<User> friendsList = new ArrayList<>();
//
//        for (Long friend : allUsers().get(user).getFriends()) {
//            friendsList.add(allUsers().get(friend));
//        }
//        return friendsList;
//    }
//
//}
