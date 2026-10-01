package com.automation.utils;
 
import java.util.HashMap;
import java.util.Map;
 
public class ScenarioContext {
 
	private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);
	private static final Map<String, Object> globalContext = new java.util.concurrent.ConcurrentHashMap<>();
 
	public static void setGlobal(String key, Object value) {
		logger.info("🌍 Storing GLOBAL context: " + key + " = " + value);
		globalContext.put(key, value);
	}
 
	public static Object getGlobal(String key) {
		return globalContext.get(key);
	}
 
	private static final LoggerUtils logger = LoggerUtils.getInstance();
 
	private static final ThreadLocal<Map<String, Integer>> iterationTracker = ThreadLocal.withInitial(HashMap::new);
 
	/**
	 * Store any type of data in scenario context Examples: String, Integer,
	 * Boolean, List, Map, Set, JsonNode, DTOs etc.
	 */
	public static void set(String key, Object value) {
		logger.info(value + "stored under" + key);
		context.get().put(key, value);
	}
 
	/**
	 * Retrieve data from scenario context
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String key) {
		return (T) context.get().get(key);
	}
 
	/**
	 * Check whether key exists
	 */
	public static boolean contains(String key) {
		return context.get().containsKey(key);
	}
 
	/**
	 * Remove specific key from context
	 */
	public static void remove(String key) {
		context.get().remove(key);
	}
 
	/**
	 * Clear complete scenario context
	 */
	public static void clear() {
		context.remove();
	}
 
	public static int getNextIteration(String scenarioId) {
		Map<String, Integer> tracker = iterationTracker.get();
		int next = tracker.getOrDefault(scenarioId, 0) + 1;
		tracker.put(scenarioId, next);
		return next;
	}
 
	@SuppressWarnings("unchecked")
	public static Map<String, String> getMap(String key) {
		return (Map<String, String>) context.get().get(key);
	}
 
	public static Map<String, String> getCurrentTestData() {
		String currentTcid = getCurrentTestCaseId();
		Map<String, String> data = getMap(currentTcid);
		return data;
	}
	
	public static String getDataMap(String tcid, String testdataname) {
	    Map<String, String> data = getMap(tcid);
	    if (data == null) {
	        logger.error("No test data found for key: " + tcid);
	        return "";
	    }
	    return data.getOrDefault(testdataname, "");
	}
	
	
	public static String getTestData(String key, String testDataName) {
		// 1. Check Local Context
		Object val = get(key);
 
		// 2. Fallback to Global Context
		if (val == null || val.toString().trim().isEmpty()) {
			val = getGlobal(key);
		}
 
		if (val != null && !val.toString().trim().isEmpty()) {
			logger.info("🎯 Found [" + key + "] in Context: " + val);
			return val.toString();
		}
 
		// 3. Fallback to Excel
		String currentTcid = getCurrentTestCaseId();
		if (currentTcid == null) {
			logger.error("CURRENT_TCID is null! Cannot fallback to Excel. Make sure your @Given step sets it.");
			return "";
		}
		
		logger.info("⚠️ [" + key + "] not found in context. Falling back to Excel [" + currentTcid + "]");
		return getDataMap(currentTcid, testDataName);
	}
 
	public static String getTestData(String testDataName) {
		String currentTcid = getCurrentTestCaseId();
		if (currentTcid == null) {
			logger.error("CURRENT_TCID is null! Make sure your @Given step sets it.");
			return "";
		}
		return getDataMap(currentTcid, testDataName);
	}
	
	public static String getCurrentTestCaseId() {
		return get("CURRENT_TCID");
	}
	
	public static String getCurrentSheetName() {
		return get("CURRENT_SHEETNAME");
	}
}
 