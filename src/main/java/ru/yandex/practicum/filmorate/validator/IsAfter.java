package ru.yandex.practicum.filmorate.validator;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface IsAfter {

   String message() default "{message.key}";
   Class<?>[] groups() default {};
   Class<? extends Payload>[] payload() default {};
   String current();

}