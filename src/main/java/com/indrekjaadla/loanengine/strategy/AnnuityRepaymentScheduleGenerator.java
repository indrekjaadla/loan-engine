package com.indrekjaadla.loanengine.strategy;

import com.indrekjaadla.loanengine.domain.Loan;
import com.indrekjaadla.loanengine.domain.ScheduledPayment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL64;
import static java.math.MathContext.UNLIMITED;
import static java.math.RoundingMode.HALF_UP;

@Component
public class AnnuityRepaymentScheduleGenerator implements RepaymentScheduleGenerator {
    @Override
    public List<ScheduledPayment> generate(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan must not be null.");
        }

        BigDecimal fixedPaymentAmount = calculateFixedPaymentAmount(loan);

        return buildSchedule(loan, fixedPaymentAmount);
    }

    private BigDecimal calculateFixedPaymentAmount(Loan loan) {
        BigDecimal loanAmount = loan.getLoanAmount();
        int periodInMonths = loan.getPeriodInMonths();
        BigDecimal monthlyInterestRate = loan.getMonthlyInterestRate();

        BigDecimal onePlusRatePowN = ONE.add(monthlyInterestRate, UNLIMITED).pow(periodInMonths, UNLIMITED);
        BigDecimal numerator = loanAmount.multiply(monthlyInterestRate.multiply(onePlusRatePowN, UNLIMITED), UNLIMITED);
        BigDecimal denominator = onePlusRatePowN.subtract(ONE, UNLIMITED);

        return (numerator.divide(denominator, DECIMAL64)).setScale(2, HALF_UP);
    }

    private List<ScheduledPayment> buildSchedule(Loan loan, BigDecimal fixedPaymentAmount) {
        List<ScheduledPayment> scheduledPayments = new ArrayList<>();
        BigDecimal openingBalance = loan.getLoanAmount();
        int periodInMonths = loan.getPeriodInMonths();
        BigDecimal monthlyInterestRate = loan.getMonthlyInterestRate();

        for (int paymentNumber = 1; paymentNumber <= periodInMonths; paymentNumber++) {
            boolean isFinalPayment = paymentNumber == periodInMonths;

            ScheduledPayment payment = isFinalPayment
                    ? buildFinalPayment(paymentNumber, openingBalance, monthlyInterestRate)
                    : buildRegularPayment(paymentNumber, openingBalance, monthlyInterestRate, fixedPaymentAmount);

            scheduledPayments.add(payment);
            openingBalance = payment.remainingBalance();
        }

        return scheduledPayments;
    }

    private ScheduledPayment buildRegularPayment(int paymentNumber, BigDecimal openingBalance, BigDecimal monthlyInterestRate, BigDecimal fixedPaymentAmount) {
        BigDecimal interestAmount = calculateInterestAmount(openingBalance, monthlyInterestRate);
        BigDecimal principalAmount = fixedPaymentAmount.subtract(interestAmount);
        BigDecimal remainingBalance = openingBalance.subtract(principalAmount);

        return new ScheduledPayment(paymentNumber, fixedPaymentAmount, principalAmount, interestAmount, remainingBalance);
    }

    private ScheduledPayment buildFinalPayment(int paymentNumber, BigDecimal openingBalance, BigDecimal monthlyInterestRate) {
        BigDecimal interestAmount = calculateInterestAmount(openingBalance, monthlyInterestRate);
        BigDecimal paymentAmount = openingBalance.add(interestAmount);
        BigDecimal remainingBalance = ZERO.setScale(2, HALF_UP);

        return new ScheduledPayment(paymentNumber, paymentAmount, openingBalance, interestAmount, remainingBalance);
    }

    private BigDecimal calculateInterestAmount(BigDecimal principalAmount, BigDecimal monthlyInterestRate) {
        return principalAmount.multiply(monthlyInterestRate, UNLIMITED).setScale(2, HALF_UP);
    }
}
