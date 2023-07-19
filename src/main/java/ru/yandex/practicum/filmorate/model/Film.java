package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

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
	 
	@Past(message="Release date shall be in past")  
	@IsAfter(current = "1985-12-28")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate releaseDate;
	
	@Positive
	private Integer duration;
	
}
