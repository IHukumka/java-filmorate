package ru.yandex.practicum.filmorate.service;

import java.util.List;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDBStorage;

@Service
public class RatingDBService implements RatingService {

	private RatingDBStorage storage;

	public RatingDBService(RatingDBStorage storage) {
		this.storage = storage;
	}

	@Override
	public List<Rating> getAll() {
		return this.storage.getAll();
	}

	@Override
	public Rating create(@Valid Rating rating) {
		return this.storage.create(rating);
	}

	@Override
	public Rating edit(Integer id, @Valid Rating newRating) {
		return this.storage.edit(id, newRating);
	}

	@Override
	public Rating get(@Valid Integer id) {
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
	public Rating getRatingByFilmId(Integer id) {
		return this.storage.getRatingByFilmId(id);
	}

}
