package com.indrekjaadla.loanengine.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated {@link java.math.BigDecimal} must have exactly {@link #value()} decimal places.
 * {@code null} is considered valid.
 */
@Documented
@Constraint(validatedBy = ScaleValidator.class)
@Target({FIELD, RECORD_COMPONENT, PARAMETER})
@Retention(RUNTIME)
public @interface Scale {

    int value();

    String message() default "must have exactly {value} decimal places";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
