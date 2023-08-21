package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmService {

	public Film addLike(Integer filmId, Integer userId);

	public Film removeLike(Integer filmId, Integer userId);

	public List<Film> getTop(Integer limit);

	public Film edit(Integer id, @Valid Film newFilm);

	public ArrayList<Film> getAll();

	public Film get(Integer id);

	public Film create(@Valid Film film);

	public void clearAll();

	public boolean delete(@Valid Integer id);

}
