# Purchase Transaction Management System

This project is a Purchase Transaction Management System built using Spring Boot v3 with Java language. It allows users to store purchase transactions and retrieve them in specified currencies based on the exchange rate at the time of the transaction.

## Requirements

### 1: Store a Purchase Transaction

The application was designed accept and store a purchase transactions with the following fields:

- **Description**: A description of the purchase, not exceeding 50 characters.
- **Transaction Date**: The date of the transaction in a valid date format.
- **Purchase Amount**: The amount of the purchase in United States dollars, rounded to the nearest cent.
- **Unique Identifier**: An identifier to uniquely identify the purchase transaction.

### 2: Retrieve a Purchase Transaction in a Specified Country’s Currency

The application provides functionality to retrieve stored purchase transactions converted to currencies supported by the Treasury Reporting Rates of Exchange API. The retrieved purchase returns:

- Identifier
- Description
- Transaction Date
- Original US Dollar Purchase Amount
- Exchange Rate Used
- Converted Amount in the specified currency

#### Currency Conversion Requirements

- Use currency conversion rates from the last 6 months.
- If no conversion rate is available within 6 months before the purchase date, return an error.

## Technical Implementation

### Frameworks and Libraries

- **Spring Boot v3**: For building the application.
- **OpenFeign**: For making API calls to the Treasury Reporting Rates of Exchange API.
- **H2 Database - Embedded**: For persisting purchase transactions.
- **Swagger**: For API documentation and testing.
- **JUnit and Mockito**: For testing.
- **Spring security**: For security configuration (permit all requests).
  
### Architecture and development

- **Clean architecture structure**: The application follows a clean architecture structure to ensure separation of concerns and scalability.
- **Clean Code Practices**: The codebase adheres to clean code principles to enhance readability and maintainability.
- **Scalability**: The application is designed to scale with increasing usage and data volume.
- **Tests**: Unit tests and integration tests are included to ensure the reliability of the application.

## Getting Started

To run the application locally, follow these steps:

1. Clone the repository.
2. Navigate to the project directory.
3. Run `mvn spring-boot:run` to start the application.
4. Access the endpoints using a REST client or browser (http://localhost:8080/swagger-ui/index.html).

## Author

- [Fabiana Casagrande Costa](https://github.com/fabianatc)
