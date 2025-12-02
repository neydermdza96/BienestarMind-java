package com.Proyecto.BienestarMind.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class MayorDe14Validator implements ConstraintValidator<MayorDe14, LocalDate> {

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return false; // ya tienes @NotNull en la entidad
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 14;
    }
}