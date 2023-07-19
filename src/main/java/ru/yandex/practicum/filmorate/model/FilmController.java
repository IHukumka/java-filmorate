package ru.yandex.practicum.filmorate.model;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/films")
public class FilmController {
	
	private HashMap<Integer, Film> films;
	int counter = 0;
	
	public FilmController() {
		this.films = new HashMap<>();
		counter += films.size();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<Film> getAll() {
		ArrayList<Film> result = new ArrayList<>();
		for (Film film:this.films.values()) {
			result.add(film);
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public Film update(@Valid @RequestBody Film film) {
		if (films.containsKey(film.getId())) {
			this.films.put(film.getId(), film);
			return this.films.get(film.getId());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Film create(@Valid @RequestBody Film film) {
		counter++;
		film.setId(counter);
		this.films.put(film.getId(), film);
		return this.films.get(film.getId());
	}
}
