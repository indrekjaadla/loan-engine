# Loan Management Prototype

A prototype loan management system built for evaluating basic loan scenarios: accepting loan
parameters and generating an accurate repayment schedule. Built in response to the assignment
brief, focusing on correct, well-tested calculation logic and clear, defensible design decisions
over feature completeness.

The prototype currently supports **consumer loans** with an **annuity repayment schedule**. Other
loan types (`car loan`, `mortgage`) and one alternative repayment model (`equal principal payments`)
are represented in the domain model but intentionally not yet implemented. Additional repayment
models mentioned in the brief as optional (e.g. bullet loans, variable/fixed interest) are not yet
modeled at all. See [Scope & Tradeoffs](#scope--tradeoffs) for details and reasoning.

## Table of Contents

1. [How to Run](#how-to-run)
2. [Testing the API](#testing-the-api)
3. [Tech Stack](#tech-stack)
4. [Design Decisions](#design-decisions)
5. [Scope & Tradeoffs](#scope--tradeoffs)
6. [Path to Production / Future Iterations](#path-to-production--future-iterations)

## How to Run

### Prerequisites

- Java 21 (JDK)
- No external database or additional services required — the application runs fully in-memory

### Running the Application

Using the Gradle wrapper (no local Gradle installation needed):

```bash
./gradlew bootRun
```

The application starts on `http://localhost:8080` by default.

### Running the Tests

```bash
./gradlew test
```

Once running, see [Testing the API](#testing-the-api) below to verify the application and try
example requests.

## Testing the API

Example requests are provided in [`http/loans.http`](http/loans.http), covering both successful
scenarios and the application's error-handling behavior (unsupported loan/repayment types,
validation failures, and one known gap — see the file's comments for details on each case).

If using IntelliJ IDEA, open the file and click the green "Run" arrow next to any request — no
additional setup required. Otherwise, copy each request's method, URL, and body into any HTTP
client of your choice (Postman, curl, etc.).

### Quick example

```http
POST http://localhost:8080/api/loans
Content-Type: application/json

{
  "loanType": "CONSUMER_LOAN",
  "loanAmount": 10000.00,
  "periodInMonths": 12,
  "annualInterestRate": 12.00,
  "repaymentType": "ANNUITY"
}
```

Returns a `201 Created` response containing the created loan and its full repayment schedule.

## Tech Stack

Java 21, Spring Boot, Gradle (Groovy DSL), JUnit 5, AssertJ, Lombok.

## Design Decisions

### Money & Precision

Monetary values use `BigDecimal`, scale 2, `HALF_UP` — avoids the binary floating-point precision
issues of `double`/`float`, and `HALF_UP` matches manual rounding a borrower could verify by hand.

`annualInterestRate` must be sent with exactly two decimal places (e.g. `12.00`). Values such as
`12`, `12.5` or `12.000` are rejected with `400 Bad Request`, so callers explicitly confirm the
precision they send instead of having it silently normalized.

`annualInterestRate` is stored at scale 4 (e.g. `0.1200` for 12.00%) to preserve full precision when
converting from a 2-decimal percent input.

`MathContext.UNLIMITED` is used for `multiply`/`add`/`subtract`/`pow`, which always produces exact
results. 

`MathContext.DECIMAL64` is used for the one `divide` (the annuity formula), since division
can produce non-terminating decimals that `UNLIMITED` cannot represent.

### Repayment Schedule Model

Standard fixed-payment annuity formula: `payment = principal × [r(1+r)^n] / [(1+r)^n − 1]`, where
`r` is the monthly rate and `n` the period in months.

Rounding across many payments can leave a small drift versus the original loan amount. The **final
payment is calculated separately**: its principal absorbs the remaining balance exactly, and its
payment amount is recalculated (`interest + principal`) rather than reusing the fixed payment. The
schedule therefore always resolves to `0.00` on the final row, and the final payment may land
slightly smaller or larger than the others depending on rounding direction — both cases are
independently verified and tested.

### Domain Model Shape

`Loan` holds its schedule directly as `List<ScheduledPayment>` rather than through a separate
`Schedule` entity — MVP scope is one loan, one schedule, assigned once. `ScheduledPayment` is a
`record`: an immutable value with no identity of its own.

`Loan` validates its own invariants in its constructor and generates its own `id`, so it's never in
a partially valid state regardless of how it's constructed.

### Validation Strategy

Two layers: **Bean Validation** on `LoanRequest` validates the input contract and produces
structured 400 responses; **domain-level checks** in `Loan`'s constructor are a thin backstop that
holds regardless of what constructs a `Loan`. Loan limits (amount, period, rate) are currently
fixed, hardcoded values, applied uniformly regardless of loan type.

### Strategy Pattern for Repayment Calculation

Repayment calculation sits behind a `RepaymentScheduleGenerator` interface, with
`AnnuityRepaymentScheduleGenerator` as its only implementation. The service selects it directly —
no dispatch mechanism yet, since one implementation doesn't need one.

## Scope & Tradeoffs

The following were deliberately scoped out, in favor of a smaller, well-tested, defensible core:

- **Only `CONSUMER_LOAN` / `ANNUITY` implemented.** Other `LoanType`/`RepaymentType` values exist in
  the domain model but are explicitly rejected (400, not silently accepted or 500). Bullet loans
  and variable/fixed interest (mentioned as optional in the brief) aren't modeled at all.
- **No persistence.** The schedule is computed on demand and returned directly; nothing is saved.
  Chosen over building it partially, given persistence was explicitly optional in the brief.
- **Hardcoded, company-level validation limits.** Loan amount/period/rate bounds are fixed Bean
  Validation constraints, not externalized to configuration, and apply uniformly rather than
  per loan type (only one loan type exists today, so the distinction isn't yet meaningful).
- **Limited automated test coverage.** The repayment calculator is thoroughly unit-tested
  (including both rounding-drift directions); the service and controller layers are not yet
  covered by automated tests.
- **Known gap:** malformed/unparseable request bodies (e.g. invalid JSON, invalid enum values) fall
  through to Spring Boot's default error response rather than the application's structured error
  format — see `http/loans.http`, request 7.

## Path to Production / Future Iterations

### Adding New Loan Types / Repayment Models

Supporting additional repayment models (e.g. equal principal payments) or loan types (e.g. car
loans, mortgages) is intended to be additive: the calculation logic is already isolated behind an
interface with a single implementation, so a new model plugs in alongside the existing one rather
than requiring changes to it.

### Per-Loan-Type Validation Limits

Loan limits are currently global and fixed in code. Different loan products realistically warrant
different limits, and those limits are a business decision that should be adjustable without a
code change — both point toward externalizing them as configuration, scoped per loan type.

### Schedule Regeneration / History

The brief's optional "modify loans after creation" fits naturally as regenerating a loan's
schedule while preserving the previous one, rather than overwriting it — important for audit
purposes in a banking context, so a schedule retrieved later reflects what was actually
communicated at the time, even if the calculation logic changes afterward.

### Persistence

Persistence was scoped out entirely for MVP, in favor of a smaller, well-tested core. A production
system would need to persist both loans and their schedules.

### Rounding Strategy for Autopay-Friendly Final Payments

The final payment currently may land smaller or larger than the regular payments, depending on
rounding drift. For a borrower on autopay with a fixed debit amount, a larger-than-usual final
payment is a worse outcome than a smaller one. A rounding policy biased to always favor the
borrower would guarantee the final payment is never the larger surprise — worth adopting in a
production system.
