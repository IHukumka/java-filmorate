package ru.yandex.practicum.filmorate.controller;

import java.util.List;

import javax.validation.Valid;

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
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

@Slf4j
@RestController
@RequestMapping ("/mpa")
public class RatingController {
	
	private RatingService service;

	public RatingController(RatingService service) {
		this.service = service;
	}

	@GetMapping
	@ResponseBody
	public List<Rating> getAll() {
		log.info("Получен запрос к эндпоинту: 'GET_RATINGS'. "
				+ "Возвращен список всех фильмов.");
		return service.getAll();
	}

	@PutMapping
	@ResponseBody
	public Rating update(@Valid @RequestBody Rating newRating) {
		log.info("Получен запрос к эндпоинту: 'PUT_RATINGS'.");
		Rating rating = service.edit(newRating.getId(), newRating);
		if (rating != null) {
			log.debug("Обновлены данные рейтинга id = {}.", rating.getId());
			return rating;
		} else {
			log.warn("Рейтинг id = {} в списке не найден.", newRating.getId());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	@ResponseBody
	public Rating create(@Valid @RequestBody Rating rating) {
		Rating newRating =  this.service.create(rating);
		log.info("Получен запрос к эндпоинту: 'POST_RatingS'. "
				+ "Создана запись рейтинга {}.",
				newRating.getId());
		return newRating;
	}

	@GetMapping (value = "/{id}")
	@ResponseBody
	public Rating get(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'GET_RATINGS_ID'.");
		Rating rating = this.service.get(id);
		if (rating != null) {
			log.debug("Возвращены данные рейтинга id = {}.", id);
			return rating;
		} else {
			log.warn("Рейтинг id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping
	@ResponseBody
	public List<Rating> deleteAll() {
		log.info("Получен запрос к эндпоинту: 'DELETE_RATINGS'. "
				+ "Список жанров пуст.");
		service.clearAll();
		return service.getAll();
	}

	@DeleteMapping (value = "/{id}")
	@ResponseBody
	public boolean delete(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'DELETE_RATINGS_ID'.");
		boolean deleted = this.service.delete(id);
		if (deleted) {
			log.debug("Удалены данные рейтинга id = {}.", id);
			return deleted;
		} else {
			log.warn("Рейтинг id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}
