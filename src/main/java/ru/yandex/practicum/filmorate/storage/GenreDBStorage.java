package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Set;

import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreDBStorage {
	
	public List<Genre> getAll();

	public Genre get(Integer id);

	public void clearAll();

	public boolean delete(Integer id);

	public Genre edit(Integer id, Genre genre);

	public Genre create(Genre genre);

	public Set<Genre> getGenresByFilmId(Integer id);
}
