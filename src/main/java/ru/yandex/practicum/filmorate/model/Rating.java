package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rating {
	@NotNull
	private Integer id;
	private String name;
}
