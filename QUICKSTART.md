## Setup Instructions

### Step 1: Prerequisites
- Install Java 11 or higher
- Install Maven 3.6+
- Install Git

### Step 2: Extract Framework
Extract the provided ZIP file to your desired location.

### Step 3: Open in IDE
- Open the project in IntelliJ IDEA or Eclipse
- Let Maven download all dependencies (may take a few minutes)

### Step 4: Configure Properties
Edit `src/main/resources/config.properties` with your test environment details:
```properties
browser=chrome
base.url=http://your-app-url.com
```

### Step 5: Run Tests
Execute from terminal:
```bash
# Run all tests
mvn clean test

# Run specific feature
mvn test -Dcucumber.filter.tags="@smoke"

# Generate Allure Report
mvn allure:serve
```

### Step 6: View Reports
After test execution, reports are generated in:
- `target/cucumber-reports/` - Cucumber HTML reports
- `target/allure-results/` - Allure reports (run `mvn allure:serve` to view)

## Troubleshooting

### Maven Build Issues
```bash
mvn clean install -U
```

### WebDriver Manager Issues
Check internet connection - WebDriverManager downloads drivers automatically.

### Test Execution Issues
1. Verify base URL in config.properties
2. Check if application is running
3. Review logs in `./logs/` directory
4. Verify browser is installed and updated

## Quick Reference Commands

```bash
# Build project
mvn clean install

# Run all tests
mvn test

# Run tests with specific tag
mvn test -Dcucumber.filter.tags="@smoke"

# Run tests with specific browser
mvn test -Dbrowser=firefox

# Generate and serve Allure report
mvn allure:serve

# Clean build artifacts
mvn clean

# Run specific feature file
mvn test -Dcucumber.filter.tags="Feature:Login"

# Skip tests during build
mvn clean install -DskipTests

# Run tests in parallel (requires pom.xml configuration)
mvn test -DthreadCount=4
```

## Framework Architecture

The framework follows industry best practices:

1. **Page Object Model** - Reduces code duplication and improves maintainability
2. **BDD/Gherkin** - Enables collaboration between technical and non-technical stakeholders
3. **Separation of Concerns** - Clear separation between UI, test logic, and utilities
4. **Reusability** - Common functionality in BasePage and utilities
5. **Scalability** - Easy to add new tests, pages, and features
6. **Reporting** - Comprehensive test reports with Allure

## Next Steps

1. Add your application URL in config.properties
2. Create custom page objects for your application
3. Write BDD scenarios in feature files
4. Implement corresponding step definitions
5. Run tests and generate reports

For detailed documentation, see README.md
