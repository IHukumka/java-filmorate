package ru.yandex.practicum.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

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
	
//	@NotNull
	private String name;
	
	@Past
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate birthday;
	
}