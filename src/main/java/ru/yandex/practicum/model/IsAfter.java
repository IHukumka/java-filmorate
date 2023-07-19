package ru.yandex.practicum.model;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface IsAfter{

   String message() default "{message.key}";
   Class<?>[] groups() default {};
   Class<? extends Payload>[] payload() default {};
   String current();  
}