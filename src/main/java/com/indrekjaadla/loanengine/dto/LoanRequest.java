package com.indrekjaadla.loanengine.dto;

import com.indrekjaadla.loanengine.domain.LoanType;
import com.indrekjaadla.loanengine.domain.RepaymentType;
import com.indrekjaadla.loanengine.validation.Scale;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record LoanRequest(
        @NotNull(message = "Loan type must not be null")
        LoanType loanType,

        @NotNull(message = "Loan amount must not be null")
        @DecimalMin(value = "500.00", message = "Loan amount must be at least 500.00")
        @DecimalMax(value = "10000.00", message = "Loan amount must not exceed 10000.00")
        @Digits(integer = 5, fraction = 2, message = "Loan amount may have at most 2 decimal places")
        BigDecimal loanAmount,

        @Min(value = 3, message = "Loan period must be at least 3 months")
        @Max(value = 360, message = "Loan period must not exceed 360 months")
        int periodInMonths,

        @NotNull(message = "Annual interest rate must not be null")
        @DecimalMin(value = "5.00", message = "Annual interest rate must be at least 5.00")
        @DecimalMax(value = "50.00", message = "Annual interest rate must not exceed 50.00")
        @Scale(value = 2, message = "Annual interest rate must have exactly 2 decimal places")
        BigDecimal annualInterestRate,

        @NotNull(message = "Repayment type must not be null")
        RepaymentType repaymentType
) {
}
