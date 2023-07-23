package ru.yandex.practicum.filmorate.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping ("/users")
public class UserController {

	private Map<Integer, User> users = new HashMap<>();
	int counter = 0;

	public UserController() {
	}

	@GetMapping
	@ResponseBody
	public ArrayList<User> getAll() {
		log.info("Получен запрос к эндпоинту: 'GET_USERS'. "
				+ "Возвращен список всех пользователей.");
		return new ArrayList<User>(this.users.values());
	}

	@PostMapping
	@ResponseBody
	public User create(@Valid @RequestBody User user) {
		user.setId(getNextId());
		log.info("Получен запрос к эндпоинту: 'POST_USERS'. "
				+ "Создана запись пользователя {}.",
				user.getId());
		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
		this.users.put(user.getId(), user);
		return this.users.get(user.getId());
	}

	@PutMapping
	@ResponseBody
	public User update(@Valid @RequestBody User user) {
		log.info("Получен запрос к эндпоинту: 'PUT_USERS'.");
		if (users.containsKey(user.getId())) {
			this.users.put(user.getId(), user);
			log.info("Обновлены данные пользователя id = {}", user.getId());
			return this.users.get(user.getId());
		} else {
			log.info("Пользователь id = {} в списке не найден.", user.getId());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	private int getNextId() {
		return counter++;
	}
}