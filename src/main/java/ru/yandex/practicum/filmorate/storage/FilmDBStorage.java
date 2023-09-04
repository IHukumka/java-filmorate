package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmDBStorage {

	public ArrayList<Film> getAll();

	public Film get(Integer id);

	public void clearAll();

	public boolean delete(Integer id);

	public Film edit(Integer id, Film film);

	public Film create(Film film) ;

	List<Film> getTop(Integer limit);

	Film removeLike(Integer filmId, Integer userId);

	Film addLike(Integer filmId, Integer userId);

}
