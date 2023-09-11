package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;


import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class InMemoryUserService implements UserService {

	private UserStorage storage;

	public InMemoryUserService(UserStorage storage) {
		this.storage = storage;
	}

	public List<User> addFriend(Integer userId, Integer friendId) {
		List<User> friendPair = new ArrayList<>();
		User user = storage.get(userId);
		User friend = storage.get(friendId);
		if (user != null && friend != null) {
			if (!user.getFriends().contains(friendId)) {
				user.getFriends().add(friendId);
				friend.getFriends().add(userId);
				this.storage.edit(userId, user);
				this.storage.edit(friendId, friend);
				friendPair.add(user);
				friendPair.add(friend);
			}
		}
		return friendPair;
	}

	public List<User> unFriend(Integer userId, Integer friendId) {
		List<User> friendPair = new ArrayList<>();
		User user = storage.get(userId);
		User friend = storage.get(friendId);
		if (user != null && friend != null) {
			if (user.getFriends().contains(friendId)) {
				user.getFriends().remove(friendId);
				friend.getFriends().remove(userId);
				this.storage.edit(userId, user);
				this.storage.edit(friendId, friend);
				friendPair.add(user);
				friendPair.add(friend);
			}
		}
		return friendPair;
	}

	public List<User> getCommonFriends(Integer userId, Integer friendId) {
		List<User> result = new ArrayList<>();
		User user = storage.get(userId);
		User friend = storage.get(friendId);
		for (Integer id:user.getFriends()) {
			if (friend.getFriends().contains(id)) {
				result.add(storage.get(id));
			}
		}
		return result;
	}

	public List<User> getUserFriends(Integer userId) {
		List<User> result = new ArrayList<>();
		for (Integer friendId:this.storage.get(userId).getFriends()) {
			result.add(this.get(friendId));
		}
		return result;
	}

	public List<User> getAll() {
		return storage.getAll();
	}

	public User create(@Valid User user) {
		return storage.create(user);
	}

	public User edit(Integer id, @Valid User newUser) {
		return storage.edit(id, newUser);
	}

	public User get(@Valid Integer id) {
		return storage.get(id);
	}

	public void clearAll() {
		storage.clearAll();
	}

	public boolean delete(@Valid Integer id) {
		return storage.delete(id);
	}

}
