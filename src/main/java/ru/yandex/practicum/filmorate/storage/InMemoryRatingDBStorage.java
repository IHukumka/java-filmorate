package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Rating;

@Slf4j
@Component
@Qualifier("InMemoryRatingDBStorage")
public class InMemoryRatingDBStorage implements RatingDBStorage {

	private final JdbcTemplate jdbcTemplate;	

	public InMemoryRatingDBStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public ArrayList<Rating> getAll() {
		SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select * from rating");
		ArrayList<Rating> result = new ArrayList<>();
		while(ratingRows.next()) {
            Rating rating = new Rating(ratingRows.getInt("id"), ratingRows.getString("name"));
            result.add(rating);
            log.debug("Найден рейтинг: {} {}", rating.getId(), rating.getName());
        }
		return result;
	}

	@Override
	public Rating get(Integer id) {
		SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select * from rating where id = " + id);
		ArrayList<Rating> result = new ArrayList<>();
		Rating rating = null;
		if(ratingRows.next()) {
            rating = new Rating(ratingRows.getInt("id"), ratingRows.getString("name"));
            result.add(rating);
            log.debug("Найден рейтинг: {} {}", rating.getId(), rating.getName());
        }
		return rating;
	}

	@Override
	public void clearAll() {
		String sql = "delete from rating";
		jdbcTemplate.execute(sql);
	}

	@Override
	public boolean delete(Integer id) {
		boolean status = false;
		jdbcTemplate.execute("delete from rating where id = " + id);
		SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * "
				+ "from rating "
				+ "where id = " +
				 id);
		if(!filmRows.next()) {
			log.debug("Удален рейтинг: {}", id);
			status = true;
		} else {
			log.error("Рейтинг с идентификатором {} не найден.", id);
		}
		return status;
	}

	@Override
	public Rating edit(Integer id, Rating rating) {
		String sql = String.format("update rating set"
			 	+ " name = '%s',"
			 	+ " where id = '%d'", rating.getName(), id);
		jdbcTemplate.execute(sql);
		return this.get(id);
	}

	@Override
	public Rating create(Rating rating) {
		String sql = String.format("insert into rating (name)"
			 	+ " values ('%s')", rating.getName());
		jdbcTemplate.execute(sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT max(id) as id from rating");
		rs.next();
		Integer id = rs.getInt("id");
		return this.get(id);
	}

	@Override
	public Rating getRatingByFilmId(Integer id) {
		Rating result = null;
		SqlRowSet rs = jdbcTemplate.queryForRowSet(
				"select r.id, r.name"
				+ " from film_rating as fr"
				+ " right join rating as r on r.id = fr.rating_id"
				+ " where fr.film_id = ?", id
				);
		if(rs.next()) {
			result = new Rating(rs.getInt("id"), rs.getString("name"));
		}
		log.debug("Получен рейтинг фильма id = {}", id);
		return result;
	}
}
