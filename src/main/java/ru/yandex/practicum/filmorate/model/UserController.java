package ru.yandex.practicum.filmorate.model;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping ("/users")
public class UserController {

	private HashMap<Integer, User> users;
	int counter = 0;

	public UserController() {
		this.users = new HashMap<>();
		counter += users.size();
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<User> getAll() {
		ArrayList<User> result = new ArrayList<>();
		for (User user:this.users.values()) {
			result.add(user);
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public User create(@Valid @RequestBody User user) {
		counter++;
		user.setId(counter);
		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
		this.users.put(user.getId(), user);
		return this.users.get(user.getId());
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public User update(@Valid @RequestBody User user) {
		if (users.containsKey(user.getId())) {
			this.users.put(user.getId(), user);
			return this.users.get(user.getId());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}