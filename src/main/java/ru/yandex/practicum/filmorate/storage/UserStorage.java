package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

	Map<Integer, User> users = new HashMap<>();

	Set<User> ratedFilms = new TreeSet<>(); 

	public ArrayList<User> getAll();

	public User get(Integer id);

	public void clearAll();

	public boolean delete(Integer id);

	public User edit(Integer id, User user);

	public User create(User user);

}