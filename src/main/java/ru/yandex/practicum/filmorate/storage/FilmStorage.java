package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

	Map<Integer, Film> films = new HashMap<>();
	
	Set<Film> ratedFilms = new TreeSet<>(); 
	
	public ArrayList<Film> getAll();
	
	public Film get(Integer id);
	
	public void clearAll();
	
	public boolean delete(Integer id);
	
	public Film edit(Integer id, Film film);
	
	public Film create(Film film);

}
