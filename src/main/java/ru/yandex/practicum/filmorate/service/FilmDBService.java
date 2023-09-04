package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDBStorage;

@Service
public class FilmDBService implements FilmService {

	private FilmDBStorage storage;

	public FilmDBService(FilmDBStorage storage,
			JdbcTemplate jdbcTemplate, UserService userService) {
		this.storage = storage;
	}

	@Override
	public Film addLike(Integer filmId, Integer userId) {
		return this.storage.addLike(filmId, userId);
	}

	@Override
	public Film removeLike(Integer filmId, Integer userId) {
		return this.storage.removeLike(filmId, userId);
	}

	@Override
	public List<Film> getTop(Integer limit) {
		return this.storage.getTop(limit);
	}

	@Override
	public Film edit(Integer id, Film newFilm) {
		return this.storage.edit(id, newFilm);
	}

	@Override
	public ArrayList<Film> getAll() {
		return this.storage.getAll();
	}

	@Override
	public Film get(Integer id) {
		return this.storage.get(id);
	}

	@Override
	public Film create(Film film)  {
		return this.storage.create(film);
	}

	@Override
	public void clearAll() {
		this.storage.clearAll();
	}

	@Override
	public boolean delete(Integer id) {
		return this.storage.delete(id);
	}

}
