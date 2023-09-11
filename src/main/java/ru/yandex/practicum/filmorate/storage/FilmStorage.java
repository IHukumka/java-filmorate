package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

	public List<Film> getAll();

	public Film get(Integer id);

	public void clearAll();

	public boolean delete(Integer id);

	public Film edit(Integer id, Film film);

	public Film create(Film film);
	
	public List<Film> getTop(Integer limit);

}