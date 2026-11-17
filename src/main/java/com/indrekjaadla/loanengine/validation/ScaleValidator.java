package com.indrekjaadla.loanengine.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ScaleValidator implements ConstraintValidator<Scale, BigDecimal> {
    private int expectedScale;

    @Override
    public void initialize(Scale constraint) {
        this.expectedScale = constraint.value();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value == null || value.scale() == expectedScale;
    }
}
