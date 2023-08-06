package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

@Data
@Builder(builderClassName = "Builder")
public class User {

	private Integer id;

	@NotBlank (message = "Email cannot be empty")
	@Email(message = "Email should be valid")
	private String email;

	@NotNull(message = "Id cannot be null")
	@NoSpaces
	private String login;

	private String name;

	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;

}