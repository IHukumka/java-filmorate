package ru.yandex.practicum.filmorate.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDBStorage;

@Service
@Qualifier("InMemoryFilmDBService")
public class InMemoryFilmDBService implements FilmService {

	@Autowired
	@Qualifier("InMemoryFilmDBStorage")
	private FilmDBStorage storage;
	
	private final JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("InMemoryUserDBService")
	private final UserService userService;

	public InMemoryFilmDBService(FilmDBStorage storage,
			JdbcTemplate jdbcTemplate, UserService userService) {
		this.storage = storage;
		this.jdbcTemplate = jdbcTemplate;
		this.userService = userService;
	}

	@Override
	public Film addLike(Integer filmId, Integer userId) {
		Film updatedFilm = this.get(filmId);
		try {
			if(this.userService.get(userId) == null) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		jdbcTemplate.execute("insert into user_likes (film_id, user_id)"
				   + " values (" + filmId + ", " + userId + ")");
		updatedFilm.getUserLikes().add(userId);
		updatedFilm.setLikes(updatedFilm.getLikes() + 1);
		return updatedFilm;
	}

	@Override
	public Film removeLike(Integer filmId, Integer userId) {
		Film updatedFilm = this.get(filmId);
		if(!this.storage.get(filmId).getUserLikes().contains(userId)) {
			return null;
		}
		jdbcTemplate.execute("delete from user_likes"
				   + " where user_id = " + userId + " and film_id = " + filmId);
		updatedFilm.getUserLikes().remove(userId);
		updatedFilm.setLikes(updatedFilm.getLikes() - 1);
		return updatedFilm;
	}

	@Override
	public List<Film> getTop(Integer limit) {
		ArrayList<Film> top = new ArrayList<>();
		
		SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("select f.id"
				+ " from film as f"
				+ " left join user_likes as ul on f.id = ul.film_id"
				+ " group by f.id"
				+ " order by count(ul.user_id) desc"
				+ " limit " + limit);
		while(filmsRows.next()) {
			top.add(this.storage.get(filmsRows.getInt("id")));
		}
		return top;
	}

	@Override
	public Film edit(Integer id, @Valid Film newFilm) {
		return this.storage.edit(id, newFilm);
	}

	@Override
	public ArrayList<Film> getAll() {
		return this.storage.getAll();
	}

	@Override
	public Film get(Integer id) {
		return this.storage.get(id);
	}

	@Override
	public Film create(@Valid Film film)  {
		return this.storage.create(film);
	}

	@Override
	public void clearAll() {
		this.storage.clearAll();
	}

	@Override
	public boolean delete(@Valid Integer id) {
		return this.storage.delete(id);
	}

}
