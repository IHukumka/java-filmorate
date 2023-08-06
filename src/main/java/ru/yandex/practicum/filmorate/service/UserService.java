package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class UserService {

	private UserStorage storage;

	public UserService (UserStorage storage) {
		this.storage = storage;
	}

	public List<User> addFriend(Integer userId, Integer friendId) {
		List<User> friendPair = new ArrayList<>();
		if (storage.get(userId) != null && storage.get(friendId) != null) {
			if (!storage.get(userId).getFriends().contains(friendId)) {
				storage.get(userId).getFriends().add(friendId);
				storage.get(friendId).getFriends().add(userId);
				friendPair.add(storage.get(userId));
				friendPair.add(storage.get(friendId));
			}
		}
		return friendPair;
	}
	
	public List<User> unFriend(Integer userId, Integer friendId) {
		List<User> friendPair = new ArrayList<>();
		if (storage.get(userId) != null && storage.get(friendId) != null) {
			if (storage.get(userId).getFriends().contains(friendId)) {
				storage.get(userId).getFriends().remove(friendId);
				storage.get(friendId).getFriends().remove(userId);
				friendPair.add(storage.get(userId));
				friendPair.add(storage.get(friendId));
			}
		}
		return friendPair;
	}

	public List<User> getCommonFriends(Integer userId, Integer friendId) {
		List<User> result = new ArrayList<>();
		for (Integer friend:storage.get(userId).getFriends()) {
			if (storage.get(friendId).getFriends().contains(friend)) {
				result.add(storage.get(friend));
			}
		}
		return result;
	}

	public List<User> getUserFriends(Integer userId){
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
