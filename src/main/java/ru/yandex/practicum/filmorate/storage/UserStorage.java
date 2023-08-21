package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

@Component
public interface UserStorage {

	public ArrayList<User> getAll();

	public User get(Integer id);

	public void clearAll();

	public boolean delete(Integer id);

	public User edit(Integer id, User user);

	public User create(User user);

}