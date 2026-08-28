package com.indrekjaadla.loanengine.dto;

import com.indrekjaadla.loanengine.domain.Loan;
import com.indrekjaadla.loanengine.domain.ScheduledPayment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.indrekjaadla.loanengine.domain.LoanType.CONSUMER_LOAN;
import static com.indrekjaadla.loanengine.domain.RepaymentType.ANNUITY;
import static org.assertj.core.api.Assertions.assertThat;

class LoanResponseTest {

    @Test
    void from_shouldMapAllFields_whenLoanHasScheduledPayments() {
        var loan = new Loan(CONSUMER_LOAN, new BigDecimal("10000.00"), 12, new BigDecimal("0.1200"), ANNUITY);
        var scheduledPayments = List.of(
                new ScheduledPayment(1,
                        new BigDecimal("888.49"),
                        new BigDecimal("788.49"),
                        new BigDecimal("100.00"),
                        new BigDecimal("9211.51"))
        );
        loan.assignScheduledPayments(scheduledPayments);

        var response = LoanResponse.from(loan);

        assertThat(response.id()).isEqualTo(loan.getId());
        assertThat(response.loanType()).isEqualTo(loan.getLoanType());
        assertThat(response.loanAmount()).isEqualTo(loan.getLoanAmount());
        assertThat(response.periodInMonths()).isEqualTo(loan.getPeriodInMonths());
        assertThat(response.annualInterestRate()).isEqualTo(new BigDecimal("12.00"));
        assertThat(response.repaymentType()).isEqualTo(loan.getRepaymentType());
        assertThat(response.scheduledPayments()).isEqualTo(loan.getScheduledPayments());
    }
}
