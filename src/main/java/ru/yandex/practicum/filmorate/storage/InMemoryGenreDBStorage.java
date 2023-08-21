package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;

@Slf4j
@Component
@Qualifier("InMemoryGenreDBStorage")
public class InMemoryGenreDBStorage implements GenreDBStorage {

	private final JdbcTemplate jdbcTemplate;

	public InMemoryGenreDBStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public ArrayList<Genre> getAll() {
		SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from Genre");
		ArrayList<Genre> result = new ArrayList<>();
		while(genreRows.next()) {
            Genre genre = new Genre(genreRows.getInt("id"), genreRows.getString("name"));
            result.add(genre);
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
        }
		return result;
	}

	@Override
	public Genre get(Integer id) {
		SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from Genre where id = " + id);
		ArrayList<Genre> result = new ArrayList<>();
		Genre genre = null;
		if(genreRows.next()) {
            genre = new Genre(genreRows.getInt("id"), genreRows.getString("name"));
            result.add(genre);
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
        }
		return genre;
	}

	@Override
	public void clearAll() {
		String sql = "delete from Genre";
		jdbcTemplate.execute(sql);
	}

	@Override
	public boolean delete(Integer id) {
		boolean status = false;
		jdbcTemplate.execute("delete from Genre where id = " + id);
		SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * "
				+ "from Genre "
				+ "where id = " +
				 id);
		if(!filmRows.next()) {
			log.info("Удален жанр: {}", id);
			status = true;
		} else {
			log.info("Жанр с идентификатором {} не найден.", id);
		}
		return status;
	}

	@Override
	public Genre edit(Integer id, Genre genre) {
		String sql = String.format("update Genre set"
			 	+ " name = '%s',"
			 	+ " where id = '%d'", genre.getName(), id);
		jdbcTemplate.execute(sql);
		return this.get(id);
	}

	@Override
	public Genre create(Genre genre) {
		String sql = String.format("insert into Genre (name)"
			 	+ " values ('%s')", genre.getName());
		jdbcTemplate.execute(sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT max(id) as id from genre");
		rs.next();
		Integer id = rs.getInt("id");
		return this.get(id);
	}

	@Override
	public Set<Genre> getGenresByFilmId(Integer id) {
		Set<Genre> result = new HashSet<>();
		SqlRowSet rs = jdbcTemplate.queryForRowSet(
				  "select g.id, g.name"
				+ " from film_genre as fg"
				+ " right join genre as g on g.id = fg.genre_id"
				+ " where fg.film_id = ?", id
						);
		while(rs.next()) {
			result.add(
					new Genre(rs.getInt("id"), rs.getString("name"))
					);
		}
		log.debug("Получен список жанров фильма id = {}", id);
		return result;
	}

}
