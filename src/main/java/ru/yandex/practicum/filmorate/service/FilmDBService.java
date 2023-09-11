package ru.yandex.practicum.filmorate.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Service
public class FilmDBService implements FilmService {

	@Autowired
	@Qualifier("FilmDBStorageImpl")
	private FilmStorage storage;
	private final UserService   userService;
	private final GenreService  genreService;
	private final RatingService ratingService;

	public FilmDBService(FilmStorage storage,
			JdbcTemplate jdbcTemplate, 
			UserService userService, 
			RatingService ratingService, 
			GenreService genreService) {
		this.storage = storage;
		this.userService = userService;
		this.genreService = genreService;
		this.ratingService = ratingService;
	}

	@Override
	public Film addLike(Integer filmId, Integer userId) {
		Film film = this.get(filmId);
		try {
			if(this.userService.get(userId) == null || film == null) {
				return null;
			} else {
				film.getUserLikes().add(userId);
				if (film.getLikes() == null) {
					film.setLikes(1);
				} else {
					film.setLikes(film.getLikes() + 1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.edit(filmId, film);
	}

	@Override
	public Film removeLike(Integer filmId, Integer userId) {
		Film film = this.get(filmId);
		if(film == null || !film.getUserLikes().contains(userId)) {
			return null;
		} else {
			film.getUserLikes().remove(userId);
			film.setLikes(film.getLikes() - 1);
		}
		return this.edit(filmId, film);
	}

	@Override
	public List<Film> getTop(Integer limit) {
		List<Film> result = this.storage.getTop(limit);
		for (Film film:result) {
			Set<Genre> genres = this.genreService.getGenresByFilmId(film.getId());
			for (Genre genre:genres) {
				film.getGenres().add(genre);
			}
			film.setMpa(this.ratingService.getRatingByFilmId(film.getId()));
		}
		return result;
	}

	@Override
	public Film edit(Integer id, Film film) {
		Film newFilm = this.storage.edit(id, film);
		if (newFilm != null) {
			Set<Genre> genres = this.genreService.getGenresByFilmId(newFilm.getId());
			for (Genre genre:genres) {
				newFilm.getGenres().add(genre);
			}
			newFilm.setMpa(this.ratingService.getRatingByFilmId(newFilm.getId()));
		}
		return newFilm;
	}

	@Override
	public List<Film> getAll() {
		List<Film> result =  this.storage.getAll();
		for (Film film:result) {
			Set<Genre> genres = this.genreService.getGenresByFilmId(film.getId());
			for (Genre genre:genres) {
				film.getGenres().add(genre);
			}
			film.setMpa(this.ratingService.getRatingByFilmId(film.getId()));
		}
		return result;
	}

	@Override
	public Film get(Integer id) {
		Film film = this.storage.get(id);
		if (film != null) {
			Set<Genre> genres = this.genreService.getGenresByFilmId(id);
			for (Genre genre:genres) {
				film.getGenres().add(genre);
			}
			film.setMpa(this.ratingService.getRatingByFilmId(id));
		}
		return film;
	}

	@Override
	public Film create(Film film)  {
		Film newFilm = this.storage.create(film);
		if (newFilm != null) {
			Set<Genre> genres = this.genreService.getGenresByFilmId(newFilm.getId());
			for (Genre genre:genres) {
				newFilm.getGenres().add(genre);
			}
			newFilm.setMpa(this.ratingService.getRatingByFilmId(newFilm.getId()));
		}
		return newFilm;
	}

	@Override
	public void clearAll() {
		this.storage.clearAll();
	}

	@Override
	public boolean delete(Integer id) {
		return this.storage.delete(id);
	}
}