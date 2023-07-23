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
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping ("/films")
public class FilmController {

	private Map<Integer, Film> films = new HashMap<>();
	int counter = 0;

	public FilmController() {
	}

	@GetMapping
	@ResponseBody
	public ArrayList<Film> getAll() {
		log.info("Получен запрос к эндпоинту: 'GET_FILMS'. "
				+ "Возвращен список всех фильмов.");
		return new ArrayList<Film>(this.films.values());
	}

	@PutMapping
	@ResponseBody
	public Film update(@Valid @RequestBody Film film) {
		log.info("Получен запрос к эндпоинту: 'PUT_FILMS'.");
		if (films.containsKey(film.getId())) {
			this.films.put(film.getId(), film);
			log.info("Обновлены данные фильма id = {}.", film.getId());
			return this.films.get(film.getId());
		} else {
			log.info("Фильм id = {} в списке не найден.", film.getId());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	@ResponseBody
	public Film create(@Valid @RequestBody Film film) {
		film.setId(getNextId());
		log.info("Получен запрос к эндпоинту: 'POST_FILMS'. "
				+ "Создана запись фильма {}.",
				film.getId());
		this.films.put(film.getId(), film);
		return this.films.get(film.getId());
	}

	private int getNextId() {
		counter++;
		return counter;
	}

}