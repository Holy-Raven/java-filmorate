package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BusinessLogicException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserServiceInterface {

    private final UserStorage userStorage;

    private final FriendshipStorage friendshipStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

//    public UserService(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
//        this.userStorage = userStorage;
//    }
//    private long newId = 1;
//
//    private long getNewId() {
//        return newId++;
//    }
//
//    public void setNewId(int newId) {
//        this.newId = newId;
//    }

    @Override
    public List<User> findAll() {

        return new ArrayList<>(userStorage.allUsers());
    }

    @Override
    public User create(User user) {

        LocalDate currentTime = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Your email cannot be empty and must contain the character @");
            throw new ValidationException("Your email cannot be empty and must contain the character @");

        } else if (user.getLogin() == null || user.getEmail().isBlank() || user.getLogin().contains(" ")) {
            log.error("Your login cannot be empty or contain spaces.");
            throw new ValidationException("Your login cannot be empty or contain spaces.");

        }  else if (user.getBirthday().isAfter(currentTime)) {
            log.error("The date of birth cannot be in the future.");
            throw new ValidationException("The date of birth cannot be in the future.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("The name to display cannot be empty. You have been assigned a name: {}", user.getLogin());
            user = new User(null, user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());

        } else {
            user = new User(null, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday());
        }

        log.info("User added : {}", user.getLogin());
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {

        if (userStorage.existsById(user.getId())) {
            userStorage.put(user);
        } else {
            log.error("There is no such user in our list of users");
            throw new UserNotFoundException("There is no such user in our list of users");
        }
        log.info("User data updated: {}", user.getLogin());
        return user;
     }

    @Override
    public User delete(User user) {

        if (userStorage.existsById(user.getId())) {
            userStorage.del(user);
        } else {
            log.error("There is no such user in our list of users");
            throw new UserNotFoundException("There is no such user in our list of users");
        }
        log.info("User deleted: {}", user.getLogin());
        return user;
    }

    @Override
    public Optional<User> findUserById(String userId) {

        long id;

        if (userId == null || userId.isBlank()) {
            throw new ValidationException("The id must not be empty");
        }

        try {
            id = parseStringInLong(userId);
        } catch (NumberFormatException e) {
            throw new ValidationException("The id must be a number");
        }

        if (id <= 0) {
            throw new ValidationException("The id must be positive");
        }

        if (userStorage.existsById(id)) {
            return userStorage.findUserById(id);
        } else {
            throw new UserNotFoundException("There is no such user in our list of users");
        }
    }

    @Override
    public User addFriends(String user1, String user2) {

        long user1Id = parseStringInLong(user1);
        long user2Id = parseStringInLong(user2);

        Friendship friendship = new Friendship(user1Id, user2Id);

        if (friendshipStorage.isExist(friendship)) {

            if (friendshipStorage.status(friendship)){
                throw new BusinessLogicException("User №" + user2Id + " and User №" + user1Id + " already friends");

            } else {
                friendshipStorage.put(friendship);
                log.info("User №" + user1Id + "and User №" + user2Id + "now friends");
            }

        } else {

            friendshipStorage.add(friendship);
            log.info("User №" + user2Id + "added to friends lists User №" + user1Id);
        }

        return findUserById(user1).get();


//        if (userStorage.allUsers().get(user1Id).getFriends().contains(user2Id)) {
//            log.error("User №" + user2Id + " and User №" + user1Id + " already friends");
//            throw new BusinessLogicException("User №" + user2Id + " and User №" + user1Id + " already friends");
//        }
//
//        log.info("User №" + user2Id + "added to friends lists User №" + user1Id);
//        findUserById(user1).getFriends().add(user2Id);
//
//        log.info("User №" + user1Id + "added to friends lists User №" + user2Id);
//        findUserById(user2).getFriends().add(user1Id);
//
//        log.info("User №" + user1Id + "and User №" + user2Id + "now friends");
//
//        return userStorage.allUsers().get(user1Id);
    }

    @Override
    public User delFriends(String user1, String user2) {

        long user1Id = parseStringInLong(user1);
        long user2Id = parseStringInLong(user2);

        Friendship friendship = new Friendship(user1Id, user2Id);

        if (friendshipStorage.isExist(friendship)) {

                log.info("User №" + user1Id + "and User №" + user2Id + " not friends anymore");
                friendshipStorage.deleteById(friendship);

        } else {
            log.error("User №" + user2Id + " and User №" + user1Id + " not friends");
            throw new BusinessLogicException("User №" + user2Id + " and User №" + user1Id + " not friends");

        }

//        if (!userStorage.allUsers().get(user1Id).getFriends().contains(user2Id)) {
//            log.error("User №" + user2Id + " and User №" + user1Id + " not friends");
//            throw new BusinessLogicException("User №" + user2Id + " and User №" + user1Id + " not friends");
//        }
//
//        log.info("User №" + user2Id + "removed from friends list User №" + user1Id);
//        //findUserById(user1).getFriends().remove(user2Id);
//
//        log.info("User №" + user1Id + "removed from friends list User №" + user2Id);
//        //findUserById(user2).getFriends().remove(user1Id);
//
//        log.info("User №" + user1Id + "and User №" + user2Id + " not friends anymore");
//
//        return userStorage.delFriends(user1Id,user2Id);

        return findUserById(user1).get();
    }

    @Override
    public List<User> friendsList(String user) {

        long userId = parseStringInLong(user);

        List<User> friendsList = new ArrayList<>();

        for (Long friend : friendshipStorage.findAllById(userId)) {
            friendsList.add(userStorage.findUserById(friend).get());
        }

        log.info("List friends User №" + userId);
        return friendsList;
//        long userId = parseStringInLong(user);
//
//        List<User> friendsList = new ArrayList<>();
//
//        for (Long friend : userStorage.allUsers().get(userId).getFriends()) {
//            friendsList.add(userStorage.allUsers().get(friend));
//        }
//
//        log.info("List friends User №" + userId);
//        return userStorage.friendsList(userId);

    }

    @Override
    public List<User> commonFriends(String user1, String user2) {


        long user1Id = parseStringInLong(user1);
        long user2Id = parseStringInLong(user2);

        List<User> commonFriends = new ArrayList<>();

        for (Long friend : friendshipStorage.findAllById(user1Id)) {
            if (friendshipStorage.findAllById(user1Id).contains(friend)) {
                commonFriends.add(userStorage.findUserById(friend).get());
            }
        }
        log.info("List of mutual friends User №" + user1Id + " and User №" + user2Id + "ready");
        return commonFriends;



//        long user1Id = parseStringInLong(user1);
//        long user2Id = parseStringInLong(user2);
//
//        List<User> commonFriends = new ArrayList<>();
//
//        for (Long friend : userStorage.allUsers().get(user1Id).getFriends()) {
//            if (userStorage.allUsers().get(user2Id).getFriends().contains(friend)) {
//                commonFriends.add(userStorage.allUsers().get(friend));
//            }
//        }
//        log.info("List of mutual friends User №" + user1Id + " and User №" + user2Id + "ready");
//        return commonFriends;

    }

    public Long parseStringInLong(String str) {

        long a = 0;

        try {
            a = Long.parseLong(str);
        } catch (NumberFormatException e) {
            log.error("\"" + str + "\" must be a number");
            throw new ValidationException("\"" + str + "\" must be a number");
        }

        if (a <= 0) {
            log.error("\"" + str + "\" must be positive");
            throw new UserNotFoundException("\"" + str + "\" must be positive");
        }

        return a;
    }

}
