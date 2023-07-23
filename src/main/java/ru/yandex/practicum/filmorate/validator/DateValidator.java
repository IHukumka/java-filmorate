package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<IsAfter, LocalDate> {

	   LocalDate validDate;

	   @Override
	   public void initialize(IsAfter constraintAnnotation) {
	       validDate = LocalDate.parse(constraintAnnotation.current());
	   }

	   @Override
	   public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
	       return date.isAfter(validDate);
	   }
}