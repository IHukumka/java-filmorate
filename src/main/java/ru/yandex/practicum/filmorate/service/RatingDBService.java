package ru.yandex.practicum.filmorate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDBStorage;

@Service
public class RatingDBService implements RatingService {

	@Autowired
	@Qualifier("RatingDBStorageImpl")
	private RatingDBStorage storage;

	public RatingDBService(RatingDBStorage storage) {
		this.storage = storage;
	}

	@Override
	public List<Rating> getAll() {
		return this.storage.getAll();
	}

	@Override
	public Rating create(Rating rating) {
		return this.storage.create(rating);
	}

	@Override
	public Rating edit(Integer id, Rating newRating) {
		return this.storage.edit(id, newRating);
	}

	@Override
	public Rating get(Integer id) {
		return this.storage.get(id);
	}

	@Override
	public void clearAll() {
		this.storage.clearAll();
	}

	@Override
	public boolean delete(Integer id) {
		return this.storage.delete(id);
	}

	@Override
	public Rating getRatingByFilmId(Integer id) {
		return this.storage.getRatingByFilmId(id);
	}

}
