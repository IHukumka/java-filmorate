package ru.yandex.practicum.filmorate.model;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SpaceValidator.class)
@Documented
public @interface NoSpaces {
	
	String message() default "{message.key}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
