package com.indrekjaadla.loanengine.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static com.indrekjaadla.loanengine.domain.LoanType.CONSUMER_LOAN;
import static com.indrekjaadla.loanengine.domain.RepaymentType.ANNUITY;
import static org.assertj.core.api.Assertions.assertThat;

class LoanRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void annualInterestRate_shouldBeRejected_whenItHasNoDecimalPlaces() {
        var violations = validate("12");

        assertSingleScaleViolation(violations);
    }

    @Test
    void annualInterestRate_shouldBeRejected_whenItHasOneDecimalPlace() {
        var violations = validate("12.0");

        assertSingleScaleViolation(violations);
    }

    @Test
    void annualInterestRate_shouldBeAccepted_whenItHasExactlyTwoDecimalPlaces() {
        var violations = validate("12.00");

        assertThat(violations).isEmpty();
    }

    @Test
    void annualInterestRate_shouldBeRejected_whenItHasThreeDecimalPlaces() {
        var violations = validate("12.000");

        assertSingleScaleViolation(violations);
    }

    private Set<ConstraintViolation<LoanRequest>> validate(String annualInterestRate) {
        var request = new LoanRequest(CONSUMER_LOAN,
                new BigDecimal("10000.00"),
                12,
                new BigDecimal(annualInterestRate),
                ANNUITY);
        return validator.validate(request);
    }

    private void assertSingleScaleViolation(Set<ConstraintViolation<LoanRequest>> violations) {
        assertThat(violations).singleElement().satisfies(violation -> {
            assertThat(violation.getPropertyPath()).hasToString("annualInterestRate");
            assertThat(violation.getMessage()).isEqualTo("Annual interest rate must have exactly 2 decimal places");
        });
    }
}
