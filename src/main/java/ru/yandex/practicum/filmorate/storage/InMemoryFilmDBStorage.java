package ru.yandex.practicum.filmorate.storage;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.service.UserService;

@Slf4j
@Component
@Qualifier("InMemoryFilmDBStorage")
public class InMemoryFilmDBStorage implements FilmDBStorage {

	private final JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("InMemoryGenreDBService")
	private final GenreService genreService;
	@Autowired
	@Qualifier("InMemoryRatingDBService")
	private final RatingService ratingService;
	@Autowired
	@Qualifier("InMemoryUserDBService")
	private final UserService userService;

	public InMemoryFilmDBStorage(JdbcTemplate jdbcTemplate, 
			UserService userService, 
			RatingService ratingService, 
			GenreService genreService) {
		this.jdbcTemplate = jdbcTemplate;
		this.genreService = genreService;
		this.ratingService = ratingService;
		this.userService = userService;
	}

	@Override
	public ArrayList<Film> getAll() {
		ArrayList<Film> result = new ArrayList<>();
		for (Integer id:this.getFilmIds()) {
			result.add(this.get(id));
		}
		return result;
	}

	@Override
	public Film get(Integer id) {
		Film film = this.makeFilm(id);
		if (film != null) {
			Set<Integer> userLikes = this.getFilmLikes(id);
			Set<Genre> genres = this.genreService.getGenresByFilmId(id);
			for (Integer like:userLikes) {
				film.getUserLikes().add(like);
			}
			for (Genre genre:genres) {
				film.getGenres().add(genre);
			}
			film.setMpa(this.ratingService.getRatingByFilmId(id));
			film.setLikes(userLikes.size());
		} else {
			return null;
		}
		return film;
	}

	@Override
	public void clearAll() {
		List<Integer> ids = this.getFilmIds();
		for(Integer id:ids) {
			this.delete(id);
		}
	}

	@Override
	public boolean delete(Integer id) {
		boolean status = false;
		jdbcTemplate.execute("delete from film where id = " + id);
		Film film = this.get(id);
		if(film == null) {
			log.debug("Удален фильм: {}", id);
			status = true;
		} else {
			log.error("Фильм с идентификатором {} не найден.", id);
		}
		return status;
	}

	@Override
	public Film edit(Integer id, Film film) {
		Film updatedFilm = null;
		if (this.updateFilm(id, film)) {
			this.updateUserLikes(id, film.getUserLikes());
			this.updateRating(id, film.getMpa());
			this.updateGenres(id, film.getGenres());
			updatedFilm = this.get(id);
			updatedFilm.setLikes(updatedFilm.getUserLikes().size());
		} else {
			log.error("Ошибка обновления фильма id = {}", id);
		}
		return updatedFilm;
	}

	@Override
	public Film create(Film film) {
		Film newFilm = null;
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select max(id) as max_id from film");
		rs.next();
		Integer id = rs.getInt("max_id") + 1;
		film.setId(id);
		if (this.insertFilm(id, film)) {
			this.updateUserLikes(id, film.getUserLikes());
			this.updateRating(id, film.getMpa());
			this.updateGenres(id, film.getGenres());
			newFilm = this.get(id);
			newFilm.setLikes(newFilm.getUserLikes().size());
		} else {
			log.error("Не найден фильм id = {}", id);
		}
		return newFilm;
	}

	private Film makeFilm(Integer id) {
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select * "
				+ "from film "
				+ "where id = " +
				 id);
		if (rs.next()) {
			log.debug("Найден фильм: {} {}", id, rs.getString("name"));
			return Film.builder()
					.id(rs.getInt("id"))
					.name(rs.getString("name"))
					.description(rs.getString("description"))
					.releaseDate(rs.getDate("release_date").toLocalDate())
					.duration(rs.getInt("duration"))
					.build();
		} else {
			log.error("Фильм с идентификатором {} не найден.", id);
			return null;
		}
	}

	private boolean insertFilm(Integer id, Film film) {
		boolean status = false;
		String name = film.getName();
		String description = film.getDescription();
		String releaseDate = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Integer duration = film.getDuration();

		String sql = String.format("insert into film values "
			 	+ "(%d, '%s', '%s', '%s', %d)", id, name, description, releaseDate, duration);

		try {
			jdbcTemplate.execute(sql);
			status = true;
			log.debug("Успешно обновлены основные данные фильма", id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error("Ошибка SQL запроса", id);
		}

		return status;
	}

	private boolean updateFilm(Integer id, Film film) {
		boolean status = false;
		if(!this.getFilmIds().contains(id)) {
			return status;
		}
		String name = film.getName();
		String description = film.getDescription();
		String releaseDate = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Integer duration = film.getDuration();

		String sql = String.format("update film "
				+ "set name = '%s', "
			 	+ "description = '%s', "
			 	+ "release_date = '%s', "
			 	+ "duration = %d "
			 	+ "where id = %d", name, description, releaseDate, duration, id);

		try {
			jdbcTemplate.execute(sql);
			status = true;
			log.debug("Успешно создана запись фильма", id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error("Ошибка SQL запроса", id);
		}

		return status;
	}

	private List<Integer> getFilmIds() {
		List<Integer> result = new ArrayList<>();
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select id from film");
		while (rs.next()) {
			result.add(rs.getInt("id"));
		}
		log.debug("Получен список id фильмов, хранящихся в базе");
		return result;
	}
	
	private Set<Integer> getFilmLikes(Integer id) {
		Set<Integer> result = new HashSet<>();
		SqlRowSet rs = jdbcTemplate.queryForRowSet(
				"select user_id from user_likes where film_id = ?", id
				);
		while (rs.next()) {
			result.add(rs.getInt("user_id"));
		}
		log.debug("Получен список id пользователей, поставивших лайк фильму id = {}", id);
		return result;
	}

	private boolean updateRating(Integer id, Rating rating) {
		boolean status = false;
		try {
			ratingService.get(id);
			String sql = String.format("delete film_rating"
				 	+ " where film_id = %d", id);
			jdbcTemplate.execute(sql);
			sql = String.format("insert into film_rating (film_id, rating_id) values"
				 	+ " (%d, %d)", id, rating.getId());
			jdbcTemplate.execute(sql);
			status = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.debug("Ошибка обновления рейтинга. Не найден рейтинг id = {}", id);
		}
		return status;
	}

	private boolean updateGenres(Integer id, Set<Genre> genres) {
		boolean status = false;
		for (Genre genre:genres) {
			if (genreService.get(genre.getId()) == null) {
				log.error("Ошибка обновления жанров. Не найден жанр id = {}", genre);
				return status;
			}
		}
		try {
			String sql = String.format("delete from film_genre "
				 	+ "where film_id = %d", id);
			jdbcTemplate.execute(sql);
			for (Genre genre:genres) {
				sql = String.format("insert into film_genre (film_id, genre_id) values"
				 	+ " (%d, %d)", id, genre.getId());
				jdbcTemplate.execute(sql);
			}
			if (genres.equals(this.genreService.getGenresByFilmId(id))) {
				status = true;
			}
		} catch (DataAccessException e){
			e.printStackTrace();
			log.error("Ошибка обновления жанров. Ошибка запроса SQL");
		}
		return status;
	}

	private boolean updateUserLikes(Integer id, Set<Integer> userLikes) {
		boolean status = false;
		for (Integer userId:userLikes) {
				try {
					userService.get(userId);
				} catch (SQLException e) {
					e.printStackTrace();
					log.error("Ошибка обновления лайков. Не найден пользователь id = {}", userId);
				}
		}
		try {
			String sql = String.format("delete from user_likes "
				 	+ "where film_id = %d", id);
			jdbcTemplate.execute(sql);
			for (Integer userId:userLikes) {
				sql = String.format("insert into user_likes (film_id, user_id) values"
				 	+ " (%d, %d)", id, userId);
				jdbcTemplate.execute(sql);
			}
			if (userLikes.equals(this.getFilmLikes(id))) {
				status = true;
			}
		} catch (DataAccessException e){
			e.printStackTrace();
			log.error("Ошибка обновления лайков. Ошибка запроса SQL");
		}
		return status;
	}

}