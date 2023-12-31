package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SpaceValidator implements ConstraintValidator<NoSpaces, String> {

	   @Override
	   public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
	       return !string.contains(" ");
	   }
}