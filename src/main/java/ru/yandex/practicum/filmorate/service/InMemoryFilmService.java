package ru.yandex.practicum.filmorate.service;

import java.util.List;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

public class InMemoryFilmService implements FilmService {

	private final FilmStorage storage;

	public InMemoryFilmService(FilmStorage storage) {
		this.storage = storage;
	}

	public Film addLike(Integer filmId, Integer userId) {
		Film film = storage.get(filmId);
		if (film != null) {
			Integer likes = film.getLikes();
			if (!film.getUserLikes().contains(userId)) {
				film.setLikes(likes + 1);
				film.getUserLikes().add(userId);
				this.edit(filmId, film);
			}
		}
		return film;
	}

	public Film removeLike(Integer filmId, Integer userId) {
		Film film = storage.get(filmId);
		if (film != null) {
			Integer likes = film.getLikes();
			if (film.getUserLikes().contains(userId)) {
				film.setLikes(likes - 1);
				film.getUserLikes().remove(userId);
				this.edit(filmId, film);
			} else {
				film = null;
			}
		}
		return film;
	}

	public List<Film> getTop(Integer limit) {
		return this.storage.getTop(limit);
	}

	public Film edit(Integer id, @Valid Film newFilm) {
		return storage.edit(id, newFilm);
	}

	public List<Film> getAll() {
		return storage.getAll();
	}

	public Film get(Integer id) {
		return storage.get(id);
	}

	public Film create(@Valid Film film) {
		return storage.create(film);
	}

	public void clearAll() {
		storage.clearAll();
	}

	public boolean delete(@Valid Integer id) {
		return storage.delete(id);
	}

}
