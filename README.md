# Banking Management System

A console-based banking application developed using Java and Object-Oriented Programming (OOP) principles. The project supports account creation, deposits, withdrawals, balance enquiries, and transaction history, with input validation and unit testing.

## Features

- **Account Management:** Create an account with an account number, holder name, and opening balance.
- **Deposit Money:** Deposit a valid amount and update the account balance.
- **Withdraw Money:** Withdraw money while preventing invalid amounts and insufficient-funds transactions.
- **Transaction History:** Record successful deposits and withdrawals with their amounts, timestamps, and resulting balances.
- **Input Validation:** Reject invalid account details, non-positive transaction amounts, non-finite values, and invalid transaction data.
- **Exception Handling:** Handle rejected operations without terminating the menu.
- **Encapsulation:** Protect account fields and return a copy of the transaction history.
- **Immutable Transactions:** Prevent transaction fields from being reassigned after object creation.
- **Unit Testing:** Test account operations, transaction validation, and protection of internal state using JUnit 5.

## Tech Stack

- Java 17
- Maven
- JUnit 5
- Java Collections Framework
- Java Time API

## Project Structure

```text
Bank/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       ├── Account.java
    │       ├── Transaction.java
    │       └── Main.java
    └── test/
        └── java/
            └── AccountTest.java
```

## How to Run

### Prerequisites

- JDK 17 or later
- Apache Maven

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd Bank
```

### 2. Run the unit tests

```bash
mvn test
```

### 3. Compile the application

```bash
mvn compile
```

### 4. Start the application

```bash
java -cp target/classes Main
```

## Menu Options

1. Create Account
2. Deposit Money
3. Withdraw Money
4. Check Balance
5. View Transaction History
6. Test Invalid Operations
7. Exit

## Testing

The project includes JUnit 5 tests for:

- Successful deposits and withdrawals
- Invalid deposit amounts, including zero, `NaN`, and infinity
- Insufficient funds and unchanged balances
- Successful transaction recording
- Rejection of invalid transaction data
- Protection of transaction history from external list modification

Run all tests using `mvn test`.

## Design Principles

- **Encapsulation:** Account data is private and accessed through methods.
- **Single Responsibility:** `Account` manages account operations, `Transaction` represents a transaction, and `Main` handles console interaction.
- **Defensive Copying:** The transaction history getter returns a new list instead of exposing the internal list.
- **Immutability:** Transaction fields are final and initialized through the constructor.
- **Exception Handling:** Invalid operations are rejected before they modify the account state.

## Current Limitations

- The application manages one account per program run.
- Account data is stored in memory and is lost when the program exits.
- There is no database, authentication, or network API.
- The project uses `double` for monetary values as a learning implementation; production financial applications should use an appropriate decimal monetary representation.

## Planned Enhancements

- Add support for multiple accounts.
- Introduce a database for persistent account and transaction records.
- Build a FastAPI backend.
- Add user authentication and authorization.
- Develop a web-based frontend.
- Improve monetary precision and application-level error handling.

## Project Status

**Phase 1: Core Java console application and unit testing.**

This project is being developed incrementally to strengthen Java, OOP, exception handling, and automated testing skills.