package ru.yandex.practicum.filmorate.service;

import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDBStorage;

@Service
@Qualifier("InMemoryUserDBService")
public class InMemoryUserDBService implements UserService {

	@Autowired
	@Qualifier("InMemoryUserDBStorage")
	private UserDBStorage storage;

	public InMemoryUserDBService(UserDBStorage storage) {
		this.storage = storage;
	}

	@Override
	public List<User> addFriend(Integer userId, Integer friendId) {
		return storage.addFriend(userId, friendId);
	}

	@Override
	public List<User> unFriend(Integer userId, Integer friendId) {
		return storage.removeFriend(userId, friendId);
	}

	@Override
	public List<User> getCommonFriends(Integer userId, Integer friendId) {
		return storage.getCommonFriends(userId, friendId);
	}

	@Override
	public List<User> getUserFriends(Integer userId) {
		return storage.getUserFriends(userId);
	}

	@Override
	public List<User> getAll() {
		return storage.getAll();
	}

	@Override
	public User create(@Valid User user) {
		return storage.create(user);
	}

	@Override
	public User edit(Integer id, @Valid User newUser) {
		return storage.edit(id, newUser);
	}

	@Override
	public User get(@Valid Integer id) throws SQLException {
		return storage.get(id);
	}

	@Override
	public void clearAll() {
		storage.clearAll();
	}

	@Override
	public boolean delete(@Valid Integer id) {
		return storage.delete(id);
	}

}
