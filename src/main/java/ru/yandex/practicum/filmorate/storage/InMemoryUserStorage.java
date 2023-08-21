package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

	protected final Map<Integer, User> users;

	protected Integer counter;

	public InMemoryUserStorage() {
		this.users = new HashMap<>();
		this.counter = 0;
	}

	@Override
	public ArrayList<User> getAll() {
		return new ArrayList<User>(users.values());
	}

	@Override
	public User get(Integer id) {
		User user = null;
		if (users.containsKey(id)) {
			user = users.get(id);
		}
		return user;
	}

	@Override
	public void clearAll() {
		this.users.clear();
	}

	@Override
	public boolean delete(Integer id) {
		boolean isPresent = false;
		if (users.containsKey(id)) {
			isPresent = true;
			users.remove(id);
		}
		return isPresent;
	}

	@Override
	public User edit(Integer id, User update) {
		User user = null;
		if (users.containsKey(id)) {
			user = update;
			users.put(id, user);
		}
		return user;
	}

	@Override
	public User create(User user) {
		user.setId(getNextId());
		if (user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
		users.put(user.getId(), user);
		return users.get(user.getId());
	}

	private int getNextId() {
		return ++counter;
	}
}