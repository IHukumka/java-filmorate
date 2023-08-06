package ru.yandex.practicum.filmorate.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping ("/users")
public class UserController {

	private UserService service;
	int counter = 0;

	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping
	@ResponseBody
	public List<User> getAll() {
		log.info("Получен запрос к эндпоинту: 'GET_USERS'. "
				+ "Возвращен список всех пользователей.");
		return service.getAll();
	}

	@PostMapping
	@ResponseBody
	public User create(@Valid @RequestBody User user) {
		User newUser = this.service.create(user);
		log.info("Получен запрос к эндпоинту: 'POST_USERS'. "
				+ "Создана запись пользователя {}.",
				newUser.getId());
		return service.get(newUser.getId());
	}

	@PutMapping
	@ResponseBody
	public User update(@Valid @RequestBody User newUser) {
		log.info("Получен запрос к эндпоинту: 'PUT_FILMS'.");
		User user = service.edit(newUser.getId(), newUser);
		if (user != null) {
			log.info("Обновлены данные пользователя id = {}", user.getId());
			return user;
		} else {
			log.info("Пользователь id = {} в списке не найден.", newUser.getId());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping (value = "/{id}")
	@ResponseBody
	public User get(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'GET_USERS_ID'.");
		User user = this.service.get(id);
		if (user != null) {
			log.info("Возвращены данные пользователя id = {}.", user.getId());
			return user;
		} else {
			log.info("Пользователь id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping
	@ResponseBody
	public List<User> deleteAll() {
		log.info("Получен запрос к эндпоинту: 'DELETE_USERS'. "
				+ "Список фильмов пуст.");
		service.clearAll();
		return service.getAll();
	}

	@DeleteMapping (value = "/{id}")
	@ResponseBody
	public boolean delete(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'DELETE_USERS_ID'.");
		boolean deleted = this.service.delete(id);
		if (deleted) {
			log.info("Возвращены данные пользователя id = {}.", id);
			return deleted;
		} else {
			log.info("Пользователь id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/{userId}/friends/{friendId}")
	@ResponseBody
	public List<User> addFriend(@Valid @PathVariable Integer userId,
								@Valid @PathVariable Integer friendId) {
		log.info("Получен запрос к эндпоинту: 'PUT_USERS_ADD_FRIEND'.");
		List<User> friendPair = this.service.addFriend(userId, friendId);
		if (!friendPair.isEmpty()) {
			log.info("Пользователь id = {} теперь дружит с пользователем id = {}.",
					 userId, friendId);
			return friendPair;
		} else {
			if (this.service.get(userId) == null) {
				log.info("Пользователь id = {} в списке не найден.", userId);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				log.info("Пользователь id = {} в списке не найден.", friendId);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		}
	}

	@DeleteMapping(value = "/{userId}/friends/{friendId}")
	@ResponseBody
	public List<User> deleteFriend(@Valid @PathVariable Integer userId,
								   @Valid @PathVariable Integer friendId) {
		log.info("Получен запрос к эндпоинту: 'DELETE_FILMS_ADD_FRIEND'.");
		List<User> friendPair = this.service.unFriend(userId, friendId);
		if (!friendPair.isEmpty()) {
			log.info("Пользователь id = {} теперь не дружит с пользователем id = {}.",
					 userId, friendId);
			return friendPair;
		} else {
			if (this.service.get(userId) == null) {
				log.info("Пользователь id = {} в списке не найден.", userId);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				log.info("Пользователь id = {} в списке не найден.", friendId);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		}
	}

	@GetMapping(value = "/{userId}/friends")
	@ResponseBody
	public List<User> getFriends(@Valid @PathVariable Integer userId) {
		log.info("Получен запрос к эндпоинту: 'GET_USERS_FRIENDS'. "
				+ "Возвращен список всех пользователей.");
		return service.getUserFriends(userId);
	}

	@GetMapping(value = "/{userId}/friends/common/{friendId}")
	@ResponseBody
	public List<User> getFriends(@Valid @PathVariable Integer userId,
			 					   @Valid @PathVariable Integer friendId) {
		log.info("Получен запрос к эндпоинту: 'GET_USERS_FRIENDS'. "
				+ "Возвращен список всех пользователей.");
		return service.getCommonFriends(userId, friendId);
	}
}