# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TransactBench is a Spring Boot application designed to generate mock transaction messages and publish them to RabbitMQ for testing purposes. The application generates transactions based on provider, payment rail, and currency configurations, then publishes them to the `new_transaction_message` topic.

## Technology Stack

- **Java 21** (required)
- **Spring Boot 3.5.6**
- **Maven** for dependency management
- **Lombok** for reducing boilerplate code
- **RabbitMQ** for message publishing

## Common Commands

### Building the Project

```bash
# Clean and compile
./mvnw clean compile

# Package the application
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=TransactBenchApplicationTests

# Run tests with coverage
./mvnw clean verify
```

### Maven Wrapper

This project uses Maven Wrapper (`mvnw`), so Maven doesn't need to be installed globally. Always use `./mvnw` instead of `mvn`.

## Architecture

### Transaction Generation Flow

1. **Controller** accepts input parameters:
   - `provider` - The payment provider
   - `payment_rails` - The payment rail type
   - `No of messages` - Number of transactions to generate

2. **Configuration Lookup**: Based on provider and payment_rails, the system retrieves:
   - Currency
   - VAN (Virtual Account Number) from configuration

3. **Transaction Generation**: Creates N transactions with the following structure:
   - Unique `id` and `tracking_id`
   - Credit/debit indicator
   - Amount and currency
   - Value date
   - Payment rail and provider information
   - Origin and destination account details (IBAN, BIC, account numbers)
   - Sender information (name, address, country)

4. **Message Publishing**: Transactions are published to RabbitMQ topic `new_transaction_message`

5. **Response**: Returns a list of generated transaction IDs

### Transaction Message Schema

The generated transaction follows this structure:
- `id`, `version`, `credit_debit`, `amount`, `currency`
- `value_date`, `tracking_id`, `reference`
- `payment_rail`, `provider`
- `origin_account`: account_number, bic, iban
- `destination_account`: account_number
- `sender`: free_text, name, ultimate_sender_name, address, country
- `transaction_content`: nullable field

### Package Structure

- `com.currencycloud.transactbench` - Root package
  - Main application entry point: `TransactBenchApplication.java`
  - Controllers should handle transaction generation requests
  - Services should contain business logic for transaction generation
  - Configuration should map provider/payment_rail to currency/VAN
  - RabbitMQ publishers should handle message publishing

## Development Notes

- The project uses **Lombok**, so ensure annotation processing is enabled in your IDE
- Configuration for provider/payment_rail mappings should be externalized (likely in a config file or properties)
- Transaction IDs should be unique for each generated transaction
- The controller is designed to be called from step definition files (likely for BDD testing)