package ru.yandex.practicum.filmorate.storage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

@Slf4j
@Repository
@Qualifier("FilmDBStorageImpl")
public class FilmDBStorageImpl implements FilmStorage {

	private final JdbcTemplate jdbcTemplate;

	public FilmDBStorageImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Film> getAll() {
		List<Film> result = new ArrayList<>();
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from film");
		while (rs.next()) {
			result.add(this.makeFilm(rs));
		}
		return result;
	}

	@Override
	public List<Film> getTop(Integer limit) {
		List<Film> result = new ArrayList<>();
		SqlRowSet rs = jdbcTemplate.queryForRowSet(
				"select f.id,"
			  + " f.name,"
			  + " f.description,"
			  + " f.release_date,"
			  + " f.duration"
			  + " from film as f"
			  + " left join user_likes as ul on ul.film_id = f.id"
			  + " group by f.id"
			  + " order by count(ul.id) desc"
			  + " limit ?", limit);
		while(rs.next()) {
			result.add(this.makeFilm(rs));
		}
		return result;
	}

	@Override
	public Film get(Integer id) {
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select * "
				+ "from film "
				+ "where id = " +
				 id);
		if (rs.next()) {
			log.debug("Найден фильм: {} {}", id, rs.getString("name"));
			Film film = this.makeFilm(rs);
			film = this.addFilmLikes(film);
			film.setLikes(film.getUserLikes().size());
			return film;
		} else {
			log.error("Фильм с идентификатором {} не найден.", id);
			return null;
		}
	}

	@Override
	public void clearAll() {
		jdbcTemplate.execute("delete from film");
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
			updatedFilm.setLikes(film.getUserLikes().size());
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
		if (this.insertFilm(id, film)){
			this.updateUserLikes(id, film.getUserLikes());
			this.updateRating(id, film.getMpa());
			this.updateGenres(id, film.getGenres());
			newFilm = this.get(id);
			newFilm.setLikes(newFilm.getUserLikes().size());
		} else {
			log.error("Не найден фильм id = {}", id);
		};
		return newFilm;
	}

	private Film makeFilm(SqlRowSet rs) {
		Film film = Film.builder()
				.id(rs.getInt("id"))
				.name(rs.getString("name"))
				.description(rs.getString("description"))
				.releaseDate(rs.getDate("release_date").toLocalDate())
				.duration(rs.getInt("duration"))
				.build();
		film = this.addFilmLikes(film);
		film.setLikes(film.getUserLikes().size());
		return film;
		
	}

	private boolean insertFilm(Integer id, Film film) {
		boolean status = false;
		String query = String.format("insert into film values (?, ?, ?, ?, ?)");
		try {
			jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
				@Override
				public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					ps.setInt(1, id);
					ps.setString(2, film.getName());
					ps.setString(3, film.getDescription());
					ps.setString(4, film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
					ps.setInt(5, film.getDuration());
					return ps.execute();
				}
			});
			status = true;
			log.debug("Успешно обновлены основные данные фильма", id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error("Ошибка SQL запроса", id);
		}
		return status;
	}

	@SuppressWarnings("serial")
	private boolean updateFilm(Integer id, Film film) {
		boolean status = false;
		String query = "update film "
				+ "set name = ?, "
			 	+ "description = ?, "
			 	+ "release_date = ?, "
			 	+ "duration = ? "
			 	+ "where id = ?";
		try {
			if (this.get(id) == null) {
				throw new DataRetrievalFailureException("Фильм не найден.") {};
			};
			jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
				@Override
				public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					ps.setString(1, film.getName());
					ps.setString(2, film.getDescription());
					ps.setString(3, film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
					ps.setInt(4, film.getDuration());
					ps.setInt(5, id);
					return ps.execute();
				}
			});
			status = true;
			log.debug("Успешно создана запись фильма", id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error("Ошибка SQL запроса", id);
		}
		return status;
	}

	private boolean updateRating(Integer id, Rating rating) {
		boolean status = false;
		try {
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
		try {
			String sql = String.format("delete from film_genre "
				 	+ "where film_id = %d", id);
			jdbcTemplate.execute(sql);
			jdbcTemplate.batchUpdate(
					"insert into film_genre (film_id, genre_id) values (?, ?)", 
					genres, 
					genres.size(), 
					(PreparedStatement ps, Genre genre) -> {
						ps.setInt(1, id);
						ps.setInt(2, genre.getId());
					});
			status = true;
		} catch (DataAccessException e){
			e.printStackTrace();
			log.error("Ошибка обновления жанров. Ошибка запроса SQL");
		}
		return status;
	}

	private boolean updateUserLikes(Integer id, Set<Integer> userLikes) {
		boolean status = false;
		try {
			String sql = String.format("delete from user_likes "
				 	+ "where film_id = %d", id);
			jdbcTemplate.execute(sql);
			jdbcTemplate.batchUpdate(
					"insert into user_likes (film_id, user_id) values (?, ?)", 
					userLikes, 
					userLikes.size(), 
					(PreparedStatement ps, Integer userId) -> {
						ps.setInt(1, id);
						ps.setInt(2, userId);
					});
			status = true;
		} catch (DataAccessException e){
			e.printStackTrace();
			log.error("Ошибка обновления лайков. Ошибка запроса SQL");
		}
		return status;
	}
	
	private Film addFilmLikes(Film film) {
		try {
			String sql = String.format("select user_id from user_likes "
				 	+ "where film_id = %d", film.getId());
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while(rs.next()) {
				film.getUserLikes().add(rs.getInt("user_id"));
			}
		} catch (DataAccessException e){
			e.printStackTrace();
			log.error("Ошибка обновления лайков. Ошибка запроса SQL");
		}
		return film;
	}
	
}