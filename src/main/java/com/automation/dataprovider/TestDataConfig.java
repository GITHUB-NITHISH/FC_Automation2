package com.automation.dataprovider;

import java.util.HashMap;
import java.util.Map;

/**
 * TestDataConfig - Configuration class to map test cases to their data row ranges
 */
public class TestDataConfig {

    // Map of test case name to row range [startRow, endRow]
    private static final Map<String, int[]> TEST_DATA_RANGES = new HashMap<>();

    static {
        // Configure row ranges for each test case
        // Format: TEST_DATA_RANGES.put("TestCaseName", new int[]{startRow, endRow});
        // Note: Row numbers are 1-based (Excel row 1 = data row 1)

        TEST_DATA_RANGES.put("New ap account creation", new int[]{2, 3});           // Rows 1-5
        TEST_DATA_RANGES.put("createApAccountNegative", new int[]{4, 5}); // Rows 8-15
        TEST_DATA_RANGES.put("duplicateApAccount", new int[]{6, 7}); // Rows 8-15
        TEST_DATA_RANGES.put("duplicateApAccountNegative", new int[]{8, 9}); // Rows 8-15
        TEST_DATA_RANGES.put("searchApAccountWithAccount", new int[]{10, 11}); // Rows 8-15
        TEST_DATA_RANGES.put("searchApAccountWithPayee", new int[]{12, 13}); // Rows 8-15
        TEST_DATA_RANGES.put("searchApAccountWithAccountPayee", new int[]{14, 15}); // Rows 8-15
        TEST_DATA_RANGES.put("archiveApAccount", new int[]{16, 17}); // Rows 8-15
        TEST_DATA_RANGES.put("restoreApAccount", new int[]{18, 19}); // Rows 8-15
        TEST_DATA_RANGES.put("deleteApAccount", new int[]{20, 21}); // Rows 8-15
        TEST_DATA_RANGES.put("deleteApAccountNegative", new int[]{22}); // Rows 8-15



        // Add more test cases as needed
        // TEST_DATA_RANGES.put("YourTestCaseName", new int[]{startRow, endRow});
    }

    /**
     * Get row range for a test case
     * @param testCaseName Name of the test case
     * @return int array with [startRow, endRow] or null if not found
     */
    public static int[] getRowRange(String testCaseName) {
        return TEST_DATA_RANGES.get(testCaseName);
    }

    /**
     * Add or update a test case row range
     * @param testCaseName Name of the test case
     * @param startRow Starting row number (1-based)
     * @param endRow Ending row number (1-based)
     */
    public static void setRowRange(String testCaseName, int startRow, int endRow) {
        TEST_DATA_RANGES.put(testCaseName, new int[]{startRow, endRow});
    }

    /**
     * Check if a test case has configured data range
     * @param testCaseName Name of the test case
     * @return true if configured, false otherwise
     */
    public static boolean hasDataRange(String testCaseName) {
        return TEST_DATA_RANGES.containsKey(testCaseName);
    }

    /**
     * Get all configured test cases
     * @return Map of test case names to row ranges
     */
    public static Map<String, int[]> getAllTestDataRanges() {
        return new HashMap<>(TEST_DATA_RANGES);
    }
}