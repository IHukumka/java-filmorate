package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Film {

	private Integer id;

	@NotNull(message = "Name cannot be null")
	@NotBlank(message = "Name cannot be empty")
	private String name;

	@Size(min = 10, max = 200, message
		      = "Description must be between 10 and 200 characters")
	private String description;

	@NotNull(message = "Date cannot be null")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "Release date shall be in past")
	@IsAfter(current = "1985-12-28", message = "Release date shall be after 28.12.1985")
	private LocalDate releaseDate;

	@Positive
	private Integer duration;

}