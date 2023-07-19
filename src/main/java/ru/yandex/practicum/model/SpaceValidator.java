package ru.yandex.practicum.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SpaceValidator implements ConstraintValidator<NoSpaces, String> {;

	   @Override
	   public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
	       return !string.contains(" ");
	   }

}