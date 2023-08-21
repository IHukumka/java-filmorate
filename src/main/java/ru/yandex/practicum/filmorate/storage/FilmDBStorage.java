package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmDBStorage {

	public ArrayList<Film> getAll();

	public Film get(Integer id);

	public void clearAll();

	public boolean delete(Integer id);

	public Film edit(Integer id, Film film);

	public Film create(Film film) ;

}
