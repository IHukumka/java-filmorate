package ru.yandex.practicum.filmorate.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

@Slf4j
@RestController
@RequestMapping ("/genres")
public class GenreController {
	
	@Autowired
	@Qualifier("InMemoryGenreDBService")
	private GenreService service;

	public GenreController(GenreService service) {
		this.service = service;
	}

	@GetMapping
	@ResponseBody
	public List<Genre> getAll() {
		log.info("Получен запрос к эндпоинту: 'GET_GENRES'. "
				+ "Возвращен список всех фильмов.");
		return service.getAll();
	}

	@PutMapping
	@ResponseBody
	public Genre update(@Valid @RequestBody Genre newGenre) {
		log.info("Получен запрос к эндпоинту: 'PUT_GENRES'.");
		Genre genre = service.edit(newGenre.getId(), newGenre);
		if (genre != null) {
			log.debug("Обновлены данные жанра id = {}.", genre.getId());
			return genre;
		} else {
			log.warn("Жанр id = {} в списке не найден.", newGenre.getId());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	@ResponseBody
	public Genre create(@Valid @RequestBody Genre genre) {
		Genre newGenre =  this.service.create(genre);
		log.info("Получен запрос к эндпоинту: 'POST_GENRES'. "
				+ "Создана запись жанра {}.",
				newGenre.getId());
		return newGenre;
	}

	@GetMapping (value = "/{id}")
	@ResponseBody
	public Genre get(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'GET_GENRES_ID'.");
		Genre genre = this.service.get(id);
		if (genre != null) {
			log.debug("Возвращены данные жанра id = {}.", id);
			return genre;
		} else {
			log.warn("Жанр id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping
	@ResponseBody
	public List<Genre> deleteAll() {
		log.info("Получен запрос к эндпоинту: 'DELETE_GENRES'. "
				+ "Список жанров пуст.");
		service.clearAll();
		return service.getAll();
	}

	@DeleteMapping (value = "/{id}")
	@ResponseBody
	public boolean delete(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'DELETE_GENRES_ID'.");
		boolean deleted = this.service.delete(id);
		if (deleted) {
			log.debug("Удалены данные жанра id = {}.", id);
			return deleted;
		} else {
			log.warn("Жанр id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}
