# Multiple Data Sets per Test Case Implementation

This implementation allows you to load multiple rows from a single Excel sheet for different test cases, where each test case gets its own row range.

## How It Works

1. **TestDataConfig.java** - Maps test case names to row ranges
2. **ExcelUtil.java** - Extended to read multiple rows with `getRowsData(startRow, endRow)`
3. **ScenarioContext.java** - Updated to store `List<Map<String, String>>` for multiple data sets
4. **TestDataIterator.java** - Utility to iterate through data sets
5. **CucumberHooks.java** - Automatically loads data based on scenario name

## Setup Instructions

### 1. Configure Test Data Ranges

Edit `TestDataConfig.java` to map your test cases to row ranges:

```java
TEST_DATA_RANGES.put("CreateApAccount", new int[]{1, 5});           // Rows 1-5
TEST_DATA_RANGES.put("ValidateApAccountDeletion", new int[]{8, 15}); // Rows 8-15
```

### 2. Excel Sheet Structure

Your Excel sheet should have:
- Row 0: Headers (column names)
- Rows 1+: Data rows

Example:
```
| AccountName | AccountType | Description |
|-------------|-------------|-------------|
| Account1    | TypeA       | Test data 1 |
| Account2    | TypeB       | Test data 2 |
| Account3    | TypeA       | Test data 3 |
| Account4    | TypeC       | Test data 4 |
| Account5    | TypeB       | Test data 5 |
| ...         | ...         | ...         |
| Account8    | TypeD       | Delete test 1 |
| Account9    | TypeE       | Delete test 2 |
| ...         | ...         | ...         |
```

### 3. Feature File

Use scenario names that match your TestDataConfig keys:

```gherkin
@ApAccountData
Feature: AP Account Testing

  Scenario: Create AP Account with multiple data sets
    Given I have multiple test data sets for "CreateApAccount"
    When I process each data set
    Then all data sets should be processed successfully
```

### 4. Step Definitions

Use TestDataIterator to access data sets:

```java
@When("I process each data set")
public void iProcessEachDataSet() {
    while (TestDataIterator.hasNextDataSet()) {
        Map<String, String> currentData = TestDataIterator.getNextDataSet();

        String accountName = currentData.get("AccountName");
        String accountType = currentData.get("AccountType");

        // Your test logic here
    }
}
```

## Key Components

### TestDataConfig
- Maps scenario names to row ranges
- Easy to maintain and update

### TestDataIterator
- `initialize(List<Map<String, String>>)` - Set data sets
- `hasNextDataSet()` - Check if more data available
- `getNextDataSet()` - Get next data set
- `getCurrentDataSet()` - Get current without advancing
- `getTotalDataSets()` - Get total count

### Backward Compatibility
- Existing single-row tests still work
- Only scenarios with configured ranges use multiple data sets

## Usage Patterns

### Pattern 1: Process All Data Sets in One Scenario
```java
while (TestDataIterator.hasNextDataSet()) {
    Map<String, String> data = TestDataIterator.getNextDataSet();
    // Process data
}
```

### Pattern 2: One Data Set Per Scenario Iteration
```java
Map<String, String> data = TestDataIterator.getCurrentDataSet();
// Process single data set
```

## Adding New Test Cases

1. Add entry to `TestDataConfig.java`:
   ```java
   TEST_DATA_RANGES.put("NewTestCase", new int[]{16, 20});
   ```

2. Add corresponding rows to your Excel sheet

3. Create feature scenario with matching name

4. Implement step definitions using TestDataIterator

## Benefits

- **Single Excel Sheet**: All test data in one place
- **Flexible Row Ranges**: Each test case gets exactly the data it needs
- **Easy Maintenance**: Update ranges in one configuration file
- **Backward Compatible**: Existing tests continue to work
- **Scalable**: Add new test cases without changing existing code