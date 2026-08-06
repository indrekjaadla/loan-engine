package com.indrekjaadla.loanengine.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.UUID;

@Getter
public class Loan {
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private final UUID id = UUID.randomUUID();
    private final LoanType loanType;
    private final BigDecimal loanAmount;
    private final int periodInMonths;
    private final BigDecimal annualInterestRate;
    private final RepaymentType repaymentType;
    private List<ScheduledPayment> scheduledPayments;

    public Loan(LoanType loanType,
                BigDecimal loanAmount,
                int periodInMonths,
                BigDecimal annualInterestRate,
                RepaymentType repaymentType
    ) {
        if (loanType == null) {
            throw new IllegalArgumentException("Loan type must not be null");
        }
        if (loanAmount == null || loanAmount.signum() <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if (periodInMonths <= 0) {
            throw new IllegalArgumentException("Period must be positive");
        }
        if (annualInterestRate == null || annualInterestRate.signum() < 0) {
            throw new IllegalArgumentException("Interest rate must not be negative");
        }
        if (repaymentType == null) {
            throw new IllegalArgumentException("Repayment type must not be null");
        }

        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.periodInMonths = periodInMonths;
        this.annualInterestRate = annualInterestRate;
        this.repaymentType = repaymentType;
    }

    public BigDecimal getMonthlyInterestRate() {
        return annualInterestRate.divide(MONTHS_IN_YEAR, MathContext.DECIMAL64);
    }

    public void assignScheduledPayments(List<ScheduledPayment> scheduledPayments) {
        if (this.scheduledPayments != null) {
            throw new IllegalStateException("Scheduled payments already assigned for this loan");
        }
        this.scheduledPayments = List.copyOf(scheduledPayments);
    }
}
