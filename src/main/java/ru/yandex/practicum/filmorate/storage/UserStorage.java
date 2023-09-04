package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
	
	public List<User> getAll();

	public User get(Integer id);

	public void clearAll();

	public boolean delete(Integer id);

	public User edit(Integer id, User user);

	public User create(User user);
}
