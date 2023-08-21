package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDBStorage;

@Service
@Qualifier("InMemoryGenreDBService")
public class InMemoryGenreDBService implements GenreService {

	@Autowired
	@Qualifier("InMemoryGenreDBStorage")
	private GenreDBStorage storage;
	

	public InMemoryGenreDBService(GenreDBStorage storage) {
		this.storage = storage;
	}

	@Override
	public List<Genre> getAll() {
		return this.storage.getAll();
	}

	@Override
	public Genre create(@Valid Genre genre) {
		return this.storage.create(genre);
	}

	@Override
	public Genre edit(Integer id, @Valid Genre newGenre) {
		return this.storage.edit(id, newGenre);
	}

	@Override
	public Genre get(@Valid Integer id) {
		return this.storage.get(id);
	}

	@Override
	public void clearAll() {
		this.storage.clearAll();
	}

	@Override
	public boolean delete(@Valid Integer id) {
		return this.storage.delete(id);
	}

	@Override
	public Set<Genre> getGenresByFilmId(Integer id) {
		return this.storage.getGenresByFilmId(id);
	}

}
