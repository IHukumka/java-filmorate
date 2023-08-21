package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreService {

	public List<Genre> getAll();

	public Genre create(@Valid Genre Genre);

	public Genre edit(Integer id, @Valid Genre newGenre);

	public Genre get(@Valid Integer id);

	public void clearAll();

	public boolean delete(@Valid Integer id);
	
	public Set<Genre> getGenresByFilmId(Integer id);
}
