package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Service
public class FilmService {

	private final FilmStorage storage;

	public FilmService(FilmStorage storage) {
		this.storage = storage;
	}

	public Film addLike(Integer filmId, Integer userId) {
		Film film = null;
		if (storage.get(filmId) != null) {
				film = storage.get(filmId);
				film.getLikes().add(userId);
				this.edit(filmId, film);
		}
		return film;
	}

	public Film removeLike(Integer filmId, Integer userId) {
		Film film = null;
		if (storage.get(filmId) != null) {
			if (storage.get(filmId).getLikes().contains(userId)) {
				film = storage.get(filmId);
				film.getLikes().remove(userId);
				this.edit(filmId, film);
			}
		}
		return film;
	}

	public List<Film> getTop(Integer limit){
		LinkedList<Film> result = new LinkedList<>();
		int counter = 0;
		for (Film film:storage.getAll()) {
			if(counter >= limit) {
				result.removeLast();
			}
			result.push(film);
			counter++;
		}
		return result;
	}

	public Film edit(Integer id, @Valid Film newFilm) {
		return storage.edit(id, newFilm);
	}

	public ArrayList<Film> getAll() {
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
