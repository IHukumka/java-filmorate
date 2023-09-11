package ru.yandex.practicum.filmorate.storage;

import java.sql.SQLException;
import java.util.List;

import ru.yandex.practicum.filmorate.model.User;

public interface UserDBStorage {

	public List<User> getAll();

	public User get(Integer id) throws SQLException;

	public void clearAll();

	public boolean delete(Integer id);

	public User edit(Integer id, User user);

	public User create(User user);

	public List<User> getUserFriends(Integer userId);

	public List<User> getCommonFriends(Integer userId, Integer friendId);

	public List<User> removeFriend(Integer userId, Integer friendId);

	public List<User> addFriend(Integer userId, Integer friendId);
}
