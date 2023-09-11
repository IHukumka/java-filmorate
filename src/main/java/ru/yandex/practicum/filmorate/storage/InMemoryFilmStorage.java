package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.yandex.practicum.filmorate.model.Film;

public class InMemoryFilmStorage implements FilmStorage {

	protected final Map<Integer, Film> films;

	protected Integer counter;

	public InMemoryFilmStorage() {
		this.films = new HashMap<>();
		this.counter = 0;
	}

	@Override
	public ArrayList<Film> getAll() {
		return new ArrayList<Film>(films.values());
	}

	@Override
	public Film get(Integer id) {
		Film film = null;
		if (films.containsKey(id)) {
			film = films.get(id);
		}
		return film;
	}

	@Override
	public void clearAll() {
		this.films.clear();
	}

	@Override
	public boolean delete(Integer id) {
		boolean isPresent = false;
		if (films.containsKey(id)) {
			isPresent = true;
			films.remove(id);
		}
		return isPresent;
	}

	@Override
	public Film edit(Integer id, Film update) {
		Film film = null;
		if (films.containsKey(id)) {
			film = update;
			if (film.getLikes() == null) {
				film.setLikes(0);
			}
			films.put(id, film);
		}
		return film;
	}

	@Override
	public Film create(Film film) {
		film.setId(getNextId());
		if (film.getLikes() == null) {
			film.setLikes(0);
		}
		films.put(film.getId(), film);
		return films.get(film.getId());
	}

	private int getNextId() {
		return ++counter;
	}

	@Override
	public List<Film> getTop(Integer limit) {
		List<Film> result = this.getAll()
				.stream()
				.sorted(Comparator.reverseOrder())
				.limit(limit)
				.collect(Collectors.toList());
		return result;
	}

}