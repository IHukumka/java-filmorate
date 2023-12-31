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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping ("/films")
public class FilmController {

	private FilmService service;
	int counter = 0;

	public FilmController(FilmService service) {
		this.service = service;
	}

	@GetMapping
	@ResponseBody
	public List<Film> getAll() {
		log.info("Получен запрос к эндпоинту: 'GET_FILMS'. "
				+ "Возвращен список всех фильмов.");
		return service.getAll();
	}

	@PutMapping
	@ResponseBody
	public Film update(@Valid @RequestBody Film newFilm) {
		log.info("Получен запрос к эндпоинту: 'PUT_FILMS', id:{}", newFilm.getId());
		Film film = service.edit(newFilm.getId(), newFilm);
		if (film != null) {
			log.debug("Обновлены данные фильма id = {}.", film.getId());
			return film;
		} else {
			log.warn("Ошибка обновления фильма id = {}.", newFilm.getId());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	@ResponseBody
	public Film create(@Valid @RequestBody Film film) {
		log.info("Получен запрос к эндпоинту: 'POST_FILMS'.");
		Film newFilm =  this.service.create(film);
		if (newFilm == null) {
			log.error("Ошибка создания записи фильма.");
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		} else {
			log.debug("Создана запись фильма id = {}.", film.getId());
		}
		return newFilm;
	}

	@GetMapping (value = "/{id}")
	@ResponseBody
	public Film get(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'GET_FILMS_ID', id:{}", id);
		Film film = this.service.get(id);
		if (film != null) {
			log.debug("Возвращены данные фильма id = {}.", film.getId());
			return film;
		} else {
			log.warn("Фильм id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping
	@ResponseBody
	public List<Film> deleteAll() {
		log.info("Получен запрос к эндпоинту: 'DELETE_FILMS'. "
				+ "Список фильмов пуст.");
		service.clearAll();
		return service.getAll();
	}

	@DeleteMapping (value = "/{id}")
	@ResponseBody
	public boolean delete(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'DELETE_FILMS_ID', id:{}", id);
		boolean deleted = this.service.delete(id);
		if (deleted) {
			log.debug("Возвращены данные фильма id = {}.", id);
			return deleted;
		} else {
			log.warn("Фильм id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/{filmId}/like/{userId}")
	@ResponseBody
	public Film like(@Valid @PathVariable Integer filmId,
					 @Valid @PathVariable Integer userId) {
		log.info("Получен запрос к эндпоинту: 'PUT_FILMS_LIKE', filmId:{}, userId:{}", filmId, userId);
		Film film = this.service.addLike(filmId, userId);
		if (film != null) {
			log.debug("Добавлен лайк пользователя {} фильму id = {}.", userId, film.getId());
			return film;
		} else {
			log.warn("Фильм id = {} в списке не найден.", filmId);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "/{filmId}/like/{userId}")
	@ResponseBody
	public Film dislike(@Valid @PathVariable Integer filmId,
					    @Valid @PathVariable Integer userId) {
		log.info("Получен запрос к эндпоинту: 'DELETE_FILMS_LIKE', filmId:{}, userId:{}", filmId, userId);
		Film film = this.service.removeLike(filmId, userId);
		if (film != null) {
			log.debug("Удален лайк пользователя {} фильму id = {}.", userId, film.getId());
			return film;
		} else {
			log.warn("Пользователь {} или фильм id = {} в списке не найден.", userId, filmId);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/popular")
	@ResponseBody
	public List<Film> getTop(@Valid @RequestParam(required = false) Integer count) {
		log.info("Получен запрос к эндпоинту: 'GET_FILMS_POPULAR'. ");
		if (count == null) {
			count = 10;
		}
		log.debug("Возвращен список из {} популярных фильмов.", count);
		return service.getTop(count);
	}
}