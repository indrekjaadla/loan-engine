package com.indrekjaadla.loanengine.strategy;

import com.indrekjaadla.loanengine.domain.Loan;
import com.indrekjaadla.loanengine.domain.ScheduledPayment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.indrekjaadla.loanengine.domain.LoanType.CONSUMER_LOAN;
import static com.indrekjaadla.loanengine.domain.RepaymentType.ANNUITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnnuityRepaymentScheduleGeneratorTest {
    private final AnnuityRepaymentScheduleGenerator generator = new AnnuityRepaymentScheduleGenerator();

    @Test
    void generate_shouldThrowException_whenLoanIsNull() {
        assertThatThrownBy(() -> generator.generate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loan must not be null.");
    }

    @Test
    void generate_shouldGenerateExpectedSchedule_lastPaymentSmallerThanOthers() {
        var loanAmount = new BigDecimal("10000.00");
        var periodInMonths = 12;
        var annualInterestRate = new BigDecimal("0.1200");
        var loan = new Loan(CONSUMER_LOAN, loanAmount, periodInMonths, annualInterestRate, ANNUITY);

        var actualScheduledPayments = generator.generate(loan);

        List<ScheduledPayment> expectedScheduledPayments = List.of(
                new ScheduledPayment(1, new BigDecimal("888.49"), new BigDecimal("788.49"), new BigDecimal("100.00"), new BigDecimal("9211.51")),
                new ScheduledPayment(2, new BigDecimal("888.49"), new BigDecimal("796.37"), new BigDecimal("92.12"), new BigDecimal("8415.14")),
                new ScheduledPayment(3, new BigDecimal("888.49"), new BigDecimal("804.34"), new BigDecimal("84.15"), new BigDecimal("7610.80")),
                new ScheduledPayment(4, new BigDecimal("888.49"), new BigDecimal("812.38"), new BigDecimal("76.11"), new BigDecimal("6798.42")),
                new ScheduledPayment(5, new BigDecimal("888.49"), new BigDecimal("820.51"), new BigDecimal("67.98"), new BigDecimal("5977.91")),
                new ScheduledPayment(6, new BigDecimal("888.49"), new BigDecimal("828.71"), new BigDecimal("59.78"), new BigDecimal("5149.20")),
                new ScheduledPayment(7, new BigDecimal("888.49"), new BigDecimal("837.00"), new BigDecimal("51.49"), new BigDecimal("4312.20")),
                new ScheduledPayment(8, new BigDecimal("888.49"), new BigDecimal("845.37"), new BigDecimal("43.12"), new BigDecimal("3466.83")),
                new ScheduledPayment(9, new BigDecimal("888.49"), new BigDecimal("853.82"), new BigDecimal("34.67"), new BigDecimal("2613.01")),
                new ScheduledPayment(10, new BigDecimal("888.49"), new BigDecimal("862.36"), new BigDecimal("26.13"), new BigDecimal("1750.65")),
                new ScheduledPayment(11, new BigDecimal("888.49"), new BigDecimal("870.98"), new BigDecimal("17.51"), new BigDecimal("879.67")),
                new ScheduledPayment(12, new BigDecimal("888.47"), new BigDecimal("879.67"), new BigDecimal("8.80"), new BigDecimal("0.00"))
        );
        assertThat(actualScheduledPayments)
                .usingRecursiveComparison()
                .isEqualTo(expectedScheduledPayments);
    }

    @Test
    void generate_shouldGenerateExpectedSchedule_lastPaymentBiggerThanOthers() {
        var loanAmount = new BigDecimal("7000.00");
        var periodInMonths = 12;
        var annualInterestRate = new BigDecimal("0.1200");
        var loan = new Loan(CONSUMER_LOAN, loanAmount, periodInMonths, annualInterestRate, ANNUITY);

        var actualScheduledPayments = generator.generate(loan);

        List<ScheduledPayment> expectedScheduledPayments = List.of(
                new ScheduledPayment(1, new BigDecimal("621.94"), new BigDecimal("551.94"), new BigDecimal("70.00"), new BigDecimal("6448.06")),
                new ScheduledPayment(2, new BigDecimal("621.94"), new BigDecimal("557.46"), new BigDecimal("64.48"), new BigDecimal("5890.60")),
                new ScheduledPayment(3, new BigDecimal("621.94"), new BigDecimal("563.03"), new BigDecimal("58.91"), new BigDecimal("5327.57")),
                new ScheduledPayment(4, new BigDecimal("621.94"), new BigDecimal("568.66"), new BigDecimal("53.28"), new BigDecimal("4758.91")),
                new ScheduledPayment(5, new BigDecimal("621.94"), new BigDecimal("574.35"), new BigDecimal("47.59"), new BigDecimal("4184.56")),
                new ScheduledPayment(6, new BigDecimal("621.94"), new BigDecimal("580.09"), new BigDecimal("41.85"), new BigDecimal("3604.47")),
                new ScheduledPayment(7, new BigDecimal("621.94"), new BigDecimal("585.90"), new BigDecimal("36.04"), new BigDecimal("3018.57")),
                new ScheduledPayment(8, new BigDecimal("621.94"), new BigDecimal("591.75"), new BigDecimal("30.19"), new BigDecimal("2426.82")),
                new ScheduledPayment(9, new BigDecimal("621.94"), new BigDecimal("597.67"), new BigDecimal("24.27"), new BigDecimal("1829.15")),
                new ScheduledPayment(10, new BigDecimal("621.94"), new BigDecimal("603.65"), new BigDecimal("18.29"), new BigDecimal("1225.50")),
                new ScheduledPayment(11, new BigDecimal("621.94"), new BigDecimal("609.68"), new BigDecimal("12.26"), new BigDecimal("615.82")),
                new ScheduledPayment(12, new BigDecimal("621.98"), new BigDecimal("615.82"), new BigDecimal("6.16"), new BigDecimal("0.00"))
        );
        assertThat(actualScheduledPayments)
                .usingRecursiveComparison()
                .isEqualTo(expectedScheduledPayments);
    }
}