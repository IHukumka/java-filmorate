package ru.yandex.practicum.filmorate.service;

import java.util.List;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.model.User;

public interface UserService {

	public List<User> addFriend(Integer userId, Integer friendId);

	public List<User> unFriend(Integer userId, Integer friendId);

	public List<User> getCommonFriends(Integer userId, Integer friendId);

	public List<User> getUserFriends(Integer userId);

	public List<User> getAll();

	public User create(@Valid User user);

	public User edit(Integer id, @Valid User newUser);

	public User get(@Valid Integer id);

	public void clearAll();

	public boolean delete(@Valid Integer id);

}
