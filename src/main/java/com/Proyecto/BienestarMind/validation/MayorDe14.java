package com.Proyecto.BienestarMind.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MayorDe14Validator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MayorDe14 {
    String message() default "Debes tener al menos 14 años";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}