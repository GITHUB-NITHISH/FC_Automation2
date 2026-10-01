# Selenium BDD Automation Framework

## Framework Overview

This is an enterprise-grade test automation framework built with:
- **Selenium WebDriver 4.x** - Browser automation
- **Cucumber 7.x** - BDD (Behavior-Driven Development)
- **TestNG 7.x** - Test execution and orchestration
- **PageFactory** - Page Object Model pattern
- **Allure Report** - Advanced test reporting
- **Maven** - Build and dependency management
- **Java 11** - Programming language

## Features

✅ **BDD with Cucumber** - Write tests in plain English using Gherkin language
✅ **Page Object Model** - Maintainable and scalable test architecture
✅ **PageFactory** - Automatic element identification and initialization
✅ **Allure Reports** - Beautiful and comprehensive test reports
✅ **Cross-browser Testing** - Chrome, Firefox, Edge, Safari support
✅ **Parallel Execution** - Run multiple tests concurrently
✅ **Data-Driven Testing** - JSON-based test data management
✅ **Logging** - Log4j2 for detailed execution logs
✅ **Screenshot on Failure** - Automatic screenshots for failed tests
✅ **Configuration Management** - Centralized configuration via properties file
✅ **WebDriver Manager** - Automatic browser driver management
✅ **Comprehensive Utilities** - Ready-to-use utility classes

## Project Structure

```
SeleniumBDDFramework/
├── pom.xml                                # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/automation/
│   │   │   ├── pages/
│   │   │   │   ├── base/
│   │   │   │   │   └── BasePage.java     # Base class for all page objects
│   │   │   │   └── web/
│   │   │   │       ├── LoginPage.java    # Login page object
│   │   │   │       ├── DashboardPage.java # Dashboard page object
│   │   │   │       └── ProductPage.java  # Product page object
│   │   │   ├── steps/
│   │   │   │   ├── LoginStepDef.java     # Login step definitions
│   │   │   │   ├── DashboardStepDef.java # Dashboard step definitions
│   │   │   │   └── ProductStepDef.java   # Product step definitions
│   │   │   ├── hooks/
│   │   │   │   └── CucumberHooks.java    # Setup and teardown logic
│   │   │   ├── runner/
│   │   │   │   └── CucumberTestRunner.java # Cucumber test runner
│   │   │   └── utils/
│   │   │       ├── DriverFactory.java    # WebDriver management
│   │   │       ├── ConfigurationManager.java # Configuration management
│   │   │       ├── LoggerUtils.java      # Logging utilities
│   │   │       ├── FileUtils.java        # File operations
│   │   │       └── JsonDataReader.java   # JSON data reader
│   │   └── resources/
│   │       ├── config.properties         # Application configuration
│   │       ├── log4j2.xml               # Logging configuration
│   │       ├── allure.properties        # Allure report configuration
│   │       ├── config/
│   │       │   └── testng.xml           # TestNG suite configuration
│   │       ├── features/
│   │       │   ├── 01_Login.feature     # Login BDD scenarios
│   │       │   ├── 02_Dashboard.feature # Dashboard BDD scenarios
│   │       │   └── 03_Product.feature   # Product BDD scenarios
│   │       ├── data/
│   │       │   └── testdata.json        # Test data in JSON format
│   │       └── locators/                # Page locators (extensible)
│   └── test/
│       └── resources/
└── reports/                             # Generated test reports
```

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd SeleniumBDDFramework
```

2. **Install dependencies**
```bash
mvn clean install
```

3. **Configure properties**
Edit `src/main/resources/config.properties`:
```properties
browser=chrome
base.url=http://localhost:8080
implicit.wait=10
explicit.wait=15
```

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific feature
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

### Run with specific browser
```bash
mvn test -Dbrowser=firefox
```

### Run with Allure reporting
```bash
mvn test
mvn allure:serve
```

### Run tests in parallel
Update `pom.xml` thread-count in surefire plugin configuration.

## Writing Tests

### 1. Create Feature File
Create `.feature` file in `src/main/resources/features/`:
```gherkin
Feature: Login functionality
  Scenario: Successful login
    Given User navigates to login page
    When User enters "username" and "password"
    Then Login should be successful
```

### 2. Create Page Object
Create page class in `src/main/java/com/automation/pages/web/`:
```java
public class LoginPage extends BasePage {
    @FindBy(id = "username")
    private WebElement usernameField;
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void login(String username, String password) {
        setText(usernameField, username);
        // ... additional actions
    }
}
```

### 3. Create Step Definitions
Create step class in `src/main/java/com/automation/steps/`:
```java
public class LoginStepDef {
    private WebDriver driver;
    private LoginPage loginPage;
    
    @When("User enters {string} and {string}")
    public void userEnters(String username, String password) {
        loginPage.login(username, password);
    }
}
```

## Framework Features Explained

### Page Object Model
- All UI elements are defined in page classes
- Separate test logic from UI interactions
- Easy maintenance and updates
- Improved code reusability

### BasePage Class
Provides common utilities:
- `waitForElementVisible()` - Wait for element visibility
- `click()` - Click elements
- `setText()` - Enter text
- `getText()` - Get text
- `selectDropdownByVisibleText()` - Select dropdowns
- `switchToIframe()` - Handle iframes
- `scrollToElement()` - Scroll to element
- `executeJavaScript()` - Execute JS

### Configuration Management
Centralized configuration through `config.properties`:
- Browser selection
- Timeouts
- Base URLs
- Screenshot options
- Report directories

### Logging
Log4j2-based logging with:
- Console logging
- File logging
- Rolling file appenders
- Error-specific logging
- Performance tracking

### Allure Reports
Generate beautiful reports:
```bash
mvn allure:serve
```
Features:
- Test execution timeline
- Step-by-step details
- Screenshots on failure
- Graphs and statistics
- Test history

## Best Practices

✅ **Use Page Object Model** - Keep pages organized and maintainable
✅ **Meaningful Assertions** - Clear assertion messages
✅ **DRY Principle** - Reuse common methods from BasePage
✅ **Logging** - Log important steps for debugging
✅ **Wait Strategies** - Use explicit waits, avoid Thread.sleep()
✅ **Data Management** - Use JSON for test data
✅ **Tagging** - Use tags for test categorization
✅ **Parallel Execution** - Configure for faster execution
✅ **Error Handling** - Proper exception handling with screenshots

## Extending the Framework

### Add New Page Object
1. Create class extending `BasePage`
2. Add `@FindBy` annotations for elements
3. Implement business logic methods

### Add New Step Definitions
1. Create class with step definition methods
2. Use `@Given`, `@When`, `@Then` annotations
3. Link to page objects

### Add New Features
1. Create `.feature` file in `src/main/resources/features/`
2. Write Gherkin scenarios
3. Implement corresponding step definitions

## Configuration Options

### config.properties

| Property | Description | Example |
|----------|-------------|---------|
| browser | Browser type | chrome, firefox, edge, safari |
| base.url | Application URL | http://localhost:8080 |
| implicit.wait | Implicit timeout in seconds | 10 |
| explicit.wait | Explicit timeout in seconds | 15 |
| headless.mode | Run browser in headless mode | true/false |
| screenshot.on.failure | Take screenshot on failure | true/false |
| video.recording | Record test execution | true/false |

## Troubleshooting

### WebDriver Issues
- Check WebDriverManager is properly configured
- Ensure browser is installed
- Verify PATH environment variables

### Test Failures
- Check logs in `./logs/` directory
- Review screenshots in `./screenshots/` directory
- Verify element locators are correct
- Check wait times and timeout values

### Allure Report Issues
- Install Allure CLI: `npm install -g allure-commandline`
- Check `target/allure-results/` directory exists
- Run `mvn allure:serve` to view reports

## Performance Optimization

1. **Parallel Execution** - Configure in pom.xml
2. **Headless Mode** - Enable in config.properties
3. **Implicit Waits** - Optimize wait times
4. **Element Caching** - Minimize DOM queries
5. **Screenshots** - Disable if not needed

## CI/CD Integration

### Jenkins
```groovy
pipeline {
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
                sh 'mvn allure:report'
            }
        }
    }
    post {
        always {
            publishAllure()
        }
    }
}
```

### GitHub Actions
```yaml
- name: Run Tests
  run: mvn clean test
- name: Generate Allure Report
  run: mvn allure:report
```

## Support and Contribution

For issues, feature requests, or contributions:
1. Create an issue on GitHub
2. Submit a pull request
3. Follow coding standards

## License

This framework is open source and available under the MIT License.

## Version History

### v1.0.0 (2024)
- Initial framework release
- Selenium 4.20 integration
- Cucumber 7.15 BDD support
- TestNG 7.8 test execution
- Allure 2.27 reporting
- PageFactory pattern implementation

---

**Happy Testing! 🚀**
