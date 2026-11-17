package com.indrekjaadla.loanengine;

import com.indrekjaadla.loanengine.domain.LoanType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.indrekjaadla.loanengine.domain.LoanType.CAR_LOAN;
import static com.indrekjaadla.loanengine.domain.LoanType.CONSUMER_LOAN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createLoan_shouldReturnCreatedLoanWithSchedule_whenRequestIsValid() throws Exception {
        String json = requestWithTypeAndRate(CONSUMER_LOAN, "12.00");
        mockMvc.perform(post("/api/loans")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.annualInterestRate").value(12.0))
                .andExpect(jsonPath("$.scheduledPayments.length()").value(12))
                .andExpect(jsonPath("$.scheduledPayments[-1].remainingBalance").value(0.0));
    }

    @Test
    void createLoan_shouldReturnBadRequest_whenAnnualInterestRateHasNoDecimalPlaces() throws Exception {
        String json = requestWithTypeAndRate(CONSUMER_LOAN, "12");
        mockMvc.perform(post("/api/loans")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message.annualInterestRate[0]").value("Annual interest rate must have exactly 2 decimal places"));
    }

    @Test
    void createLoan_shouldReturnAllValidationErrors_whenSeveralFieldsAreInvalid() throws Exception {
        String json = """
                {
                  "loanType": "CONSUMER_LOAN",
                  "loanAmount": 100.00,
                  "periodInMonths": 1,
                  "annualInterestRate": 99.00,
                  "repaymentType": "ANNUITY"
                }""";

        mockMvc.perform(post("/api/loans")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message.length()").value(3))
                .andExpect(jsonPath("$.message.loanAmount[0]").value("Loan amount must be at least 500.00"))
                .andExpect(jsonPath("$.message.periodInMonths[0]").value("Loan period must be at least 3 months"))
                .andExpect(jsonPath("$.message.annualInterestRate[0]").value("Annual interest rate must not exceed 50.00"));
    }


    @Test
    void createLoan_shouldReturnBadRequest_whenLoanTypeIsNotSupported() throws Exception {
        String json = requestWithTypeAndRate(CAR_LOAN, "12.00");
        mockMvc.perform(post("/api/loans")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Unsupported Loan Configuration"))
                .andExpect(jsonPath("$.message").value("Loan type not supported: CAR_LOAN"));
    }

    private String requestWithTypeAndRate(LoanType loanType, String annualInterestRate) {
        return """
                {
                  "loanType": "%s",
                  "loanAmount": 10000.00,
                  "periodInMonths": 12,
                  "annualInterestRate": %s,
                  "repaymentType": "ANNUITY"
                }""".formatted(loanType, annualInterestRate);
    }
}
