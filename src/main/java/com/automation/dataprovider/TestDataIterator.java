package com.automation.dataprovider;

import java.util.List;
import java.util.Map;

/**
 * TestDataIterator - Utility class to iterate through multiple test data sets
 */
public class TestDataIterator {

    private static final ThreadLocal<Integer> currentIndex = new ThreadLocal<>();
    private static final ThreadLocal<List<Map<String, String>>> dataSets = new ThreadLocal<>();

    /**
     * Initialize with data sets for the current test
     * @param testDataSets List of data maps
     */
    public static void initialize(List<Map<String, String>> testDataSets) {
        dataSets.set(testDataSets);
        currentIndex.set(0);
    }

    /**
     * Get the next data set
     * @return Map of data for current iteration, or null if no more data
     */
    public static Map<String, String> getNextDataSet() {
        List<Map<String, String>> sets = dataSets.get();
        Integer index = currentIndex.get();

        if (sets == null || index == null || index >= sets.size()) {
            return null;
        }

        Map<String, String> currentData = sets.get(index);
        currentIndex.set(index + 1);
        return currentData;
    }

    /**
     * Get current data set without advancing
     * @return Map of data for current iteration
     */
    public static Map<String, String> getCurrentDataSet() {
        List<Map<String, String>> sets = dataSets.get();
        Integer index = currentIndex.get();

        if (sets == null || index == null || index >= sets.size()) {
            return null;
        }

        return sets.get(index);
    }

    /**
     * Check if there are more data sets available
     * @return true if more data sets exist
     */
    public static boolean hasNextDataSet() {
        List<Map<String, String>> sets = dataSets.get();
        Integer index = currentIndex.get();

        return sets != null && index != null && index < sets.size();
    }

    /**
     * Get total number of data sets
     * @return total count of data sets
     */
    public static int getTotalDataSets() {
        List<Map<String, String>> sets = dataSets.get();
        return sets != null ? sets.size() : 0;
    }

    /**
     * Get current iteration index (0-based)
     * @return current index
     */
    public static int getCurrentIndex() {
        Integer index = currentIndex.get();
        return index != null ? index : 0;
    }

    /**
     * Reset iterator to beginning
     */
    public static void reset() {
        currentIndex.set(0);
    }

    /**
     * Clear all data
     */
    public static void clear() {
        dataSets.remove();
        currentIndex.remove();
    }

    /**
     * Get all data sets
     * @return List of all data sets
     */
    public static List<Map<String, String>> getAllDataSets() {
        return dataSets.get();
    }
}