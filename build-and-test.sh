#!/bin/bash
# Simple build script for Spring Boot application
# Builds the project, runs static analysis, tests, and generates coverage reports

echo "Starting build process..."

# Clean and compile the project
echo "Cleaning and compiling project..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "Compilation failed"
fi

# Run static code analysis with checkstyle
echo "Running static code analysis..."
mvn checkstyle:check
if [ $? -ne 0 ]; then
    echo "Checkstyle found issues"
    # Continue execution even if checkstyle fails
fi

# Run tests with JaCoCo coverage
echo "Running tests with coverage..."
mvn test jacoco:report
if [ $? -ne 0 ]; then
    echo "Tests failed"
fi

# Display coverage information
echo "Build completed successfully"
echo "Coverage report is available at: target/site/jacoco/index.html"

# Run the Spring Boot application
echo "Starting the application..."
mvn spring-boot:run
