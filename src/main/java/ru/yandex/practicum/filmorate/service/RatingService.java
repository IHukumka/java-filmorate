package ru.yandex.practicum.filmorate.service;

import java.util.List;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.model.Rating;

public interface RatingService {


	public List<Rating> getAll();

	public Rating create(@Valid Rating Rating);

	public Rating edit(Integer id, @Valid Rating newRating);

	public Rating get(@Valid Integer id);

	public void clearAll();

	public boolean delete(@Valid Integer id);
	
	public Rating getRatingByFilmId(Integer id);
}
