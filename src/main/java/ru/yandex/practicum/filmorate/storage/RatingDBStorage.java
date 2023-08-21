package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;

import ru.yandex.practicum.filmorate.model.Rating;


public interface RatingDBStorage {

	public ArrayList<Rating> getAll();

	public Rating get(Integer id);

	public void clearAll();

	public boolean delete(Integer id);

	public Rating edit(Integer id, Rating rating);

	public Rating create(Rating rating);

	public Rating getRatingByFilmId(Integer id);
}
