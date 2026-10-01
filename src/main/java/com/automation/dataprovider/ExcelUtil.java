package com.automation.dataprovider;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.automation.utils.ConfigurationManager;
import com.automation.utils.ScenarioContext;

public class ExcelUtil {

    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;
    
    private static String FILE_PATH = ConfigurationManager.getProperty("testdata.repo");
    
    /**
     * Load Excel Sheet
     */
    public static void loadExcel(String sheetName) {
        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found : " + sheetName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load excel : " + e.getMessage());
        }
    }

    /**
     * Read single value using column name
     */
    public static String getCellValue(String columnName) {
        try {
            Row headerRow = sheet.getRow(0);
            Row dataRow = sheet.getRow(1);
            
            int columnIndex = -1;

            for (Cell cell : headerRow) {

                if (cell.getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                    columnIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (columnIndex == -1) {
                throw new RuntimeException("Column not found : " + columnName);
            }

            Cell cell = dataRow.getCell(columnIndex);

            DataFormatter formatter = new DataFormatter();

            return formatter.formatCellValue(cell);

        } catch (Exception e) {
            throw new RuntimeException("Unable to read cell value : " + e.getMessage());
        }
    }

    /**
     * Read multiple rows as List of Maps based on row range
     */
    public static List<Map<String, String>> getRowsData(int startRow, int endRow) {
        List<Map<String, String>> dataList = new ArrayList<>();

        Row headerRow = sheet.getRow(0);
        DataFormatter formatter = new DataFormatter();

        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            Row dataRow = sheet.getRow(rowIndex);
            if (dataRow == null) continue;

            Map<String, String> rowData = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                String key = formatter.formatCellValue(headerRow.getCell(i)).trim();
                String value = formatter.formatCellValue(dataRow.getCell(i)).trim();
                rowData.put(key, value);
            }
            dataList.add(rowData);
        }

        return dataList;
    }

    /**
     * Read complete sheet row as Map
     */
    public static Map<String, String> getRowData() {

        Map<String, String> data = new HashMap<>();

        Row headerRow = sheet.getRow(0);

        Row valueRow = sheet.getRow(1);

        DataFormatter formatter = new DataFormatter();

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {

            String key = formatter.formatCellValue(headerRow.getCell(i)).trim();

            String value = formatter.formatCellValue(valueRow.getCell(i)).trim();

            data.put(key, value);
        }

        return data;
    }

    /**
     * Close workbook
     */
    public static void closeExcel() {

        try {

            if (workbook != null) {
                workbook.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get single value from sheet using column name
     */
    public static String getData(String sheetName, String keyName) {

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("Sheet not found : " + sheetName);
            }

            Row headerRow = sheet.getRow(0);

            Row valueRow = sheet.getRow(1);

            DataFormatter formatter = new DataFormatter();

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {

                String columnName =
                        formatter.formatCellValue(headerRow.getCell(i)).trim();

                if (columnName.equalsIgnoreCase(keyName)) {

                    return formatter
                            .formatCellValue(valueRow.getCell(i))
                            .trim();
                }
            }

            throw new RuntimeException(
                    "Column not found : " + keyName);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to read excel data : " + e.getMessage());
        }
    }

    /**
     * Get complete sheet data as Map
     */
    public static Map<String, String> getSheetData(String sheetName) {

        Map<String, String> data = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("Sheet not found : " + sheetName);
            }

            Row headerRow = sheet.getRow(0);

            Row valueRow = sheet.getRow(1);

            DataFormatter formatter = new DataFormatter();

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {

                String key =
                        formatter.formatCellValue(headerRow.getCell(i)).trim();

                String value =
                        formatter.formatCellValue(valueRow.getCell(i)).trim();

                data.put(key, value);
            }

            return data;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to read sheet data : " + e.getMessage());
        }
    }
    /**
     * Get iteration count for a TestCaseID in a specific sheet, filtering by 'Run' column
     */
    public static int getIterationCount(String sheetName, String testCaseID) {
        int count = 0;
        try {
            loadExcel(sheetName);
            DataFormatter formatter = new DataFormatter();
            Row headerRow = sheet.getRow(0);
            int tcIdCol = findColumnIndex(headerRow, "TestCaseID");
            if (tcIdCol == -1) tcIdCol = 0; // Fallback to 0 if not found
            
            int runColIndex = findColumnIndex(headerRow, "Run");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String rowID = formatter.formatCellValue(row.getCell(tcIdCol)).trim();
                
                if (rowID.equalsIgnoreCase(testCaseID)) {
                    String runStatus = (runColIndex != -1) ? formatter.formatCellValue(row.getCell(runColIndex)) : "Y";
                    if (runStatus.equalsIgnoreCase("Y")) {
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in getIterationCount: " + e.getMessage());
        } finally {
            closeExcel();
        }
        return count > 0 ? count : 1;
    }

    /**
     * Dynamically find which sheet contains a specific TestCaseID
     */
    public static String findSheetNameForTestCase(String testCaseID) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             XSSFWorkbook wb = new XSSFWorkbook(fis)) {
            
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                XSSFSheet currentSheet = wb.getSheetAt(i);
                Row headerRow = currentSheet.getRow(0);
                if (headerRow == null) continue;
                
                int tcIdCol = findColumnIndex(headerRow, "TestCaseID");
                if (tcIdCol == -1) tcIdCol = 0; // Fallback
                
                DataFormatter formatter = new DataFormatter();
                for (int j = 1; j <= currentSheet.getLastRowNum(); j++) {
                    Row row = currentSheet.getRow(j);
                    if (row == null) continue;
                    
                    String rowID = formatter.formatCellValue(row.getCell(tcIdCol)).trim();
                    if (rowID.equalsIgnoreCase(testCaseID)) {
                        return currentSheet.getSheetName();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to find sheet for TestCaseID " + testCaseID + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Get test data for a specific iteration of a TestCaseID, respecting 'Run' column
     */
    public static Map<String, String> getIterationData(String sheetName, String testCaseID, int iteration) {
        Map<String, String> data = new HashMap<>();
        try {
            loadExcel(sheetName);
            Row headerRow = sheet.getRow(0);
            DataFormatter formatter = new DataFormatter();
            
            int tcIdCol = findColumnIndex(headerRow, "TestCaseID");
            if (tcIdCol == -1) tcIdCol = 0; // Fallback to 0 if not found
            
            int runColIndex = findColumnIndex(headerRow, "Run");
            int validIterationCount = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String rowID = formatter.formatCellValue(row.getCell(tcIdCol)).trim();
                if (rowID.equalsIgnoreCase(testCaseID)) {
                    String runStatus = (runColIndex != -1) ? formatter.formatCellValue(row.getCell(runColIndex)) : "Y";
                    if (runStatus.equalsIgnoreCase("Y")) {
                        validIterationCount++;
                        if (validIterationCount == iteration) {
                            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                                String key = formatter.formatCellValue(headerRow.getCell(j)).trim();
                                String value = formatter.formatCellValue(row.getCell(j)).trim();
                                
                                // Only load if the key is not empty AND the value contains actual characters (not just spaces/invisibles)
                                if (!key.isEmpty() && value != null && !value.trim().isEmpty()) {
                                    data.put(key, value);
                                }
                            }
                            System.out.println("Final Filtered Map for " + testCaseID + " (Iteration " + iteration + "): " + data);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read iteration data: " + e.getMessage());
        } finally {
            closeExcel();
        }
        return data;
    }
    
    /**
     * Write test data to a specific iteration of a TestCaseID, respecting 'Run' column
     */
    public static void writeIterationData(String sheetName, String testCaseID, int iteration, Map<String, String> dataToWrite) {
        XSSFWorkbook wb = null;
        
        // Step 1: Read the workbook into memory, then close the stream immediately
        try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
            wb = new XSSFWorkbook(fis);
        } catch (Exception e) {
            System.err.println("Failed to open Excel for writing: " + e.getMessage());
            return;
        }

        // Step 2: Now operate on the in-memory workbook (fis is already closed)
        try {
            XSSFSheet currentSheet = wb.getSheet(sheetName);
            if (currentSheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row headerRow = currentSheet.getRow(0);
            DataFormatter formatter = new DataFormatter();

            int tcIdCol = findColumnIndex(headerRow, "TestCaseID");
            if (tcIdCol == -1) tcIdCol = 0;

            int runColIndex = findColumnIndex(headerRow, "Run");
            int validIterationCount = 0;
            Row targetRow = null;

            for (int i = 1; i <= currentSheet.getLastRowNum(); i++) {
                Row row = currentSheet.getRow(i);
                if (row == null) continue;

                String rowID = formatter.formatCellValue(row.getCell(tcIdCol)).trim();
                if (rowID.equalsIgnoreCase(testCaseID)) {
                    String runStatus = (runColIndex != -1) ? formatter.formatCellValue(row.getCell(runColIndex)) : "Y";
                    if (runStatus.equalsIgnoreCase("Y")) {
                        validIterationCount++;
                        if (validIterationCount == iteration) {
                            targetRow = row;
                            break;
                        }
                    }
                }
            }

            if (targetRow != null) {
                for (Map.Entry<String, String> entry : dataToWrite.entrySet()) {
                    int colIndex = findColumnIndex(headerRow, entry.getKey());
                    if (colIndex != -1) {
                        Cell cell = targetRow.getCell(colIndex);
                        if (cell == null) cell = targetRow.createCell(colIndex);
                        cell.setCellValue(entry.getValue());
                    } else {
                        System.out.println("Column not found: " + entry.getKey());
                    }
                }

                // Step 3: fis is already closed, so FileOutputStream can safely write
                try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                    wb.write(fos);
                }
                System.out.println("Data written to Excel for " + testCaseID + " iteration " + iteration);
            } else {
                System.out.println("Could not find row for " + testCaseID + " iteration " + iteration);
            }

        } catch (Exception e) {
            System.err.println("Failed to write iteration data: " + e.getMessage());
        } finally {
            try { wb.close(); } catch (Exception ignored) {}
        }
    }


    private static int findColumnIndex(Row headerRow, String columnName) {
        if (headerRow == null) return -1;
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            if (headerRow.getCell(i) != null && 
                headerRow.getCell(i).getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }
    
    public static void writeCurrentIterationData(Map<String, String> dataToWrite) {
        String testCaseId = com.automation.utils.ScenarioContext.getCurrentTestCaseId();
        if (testCaseId == null || testCaseId.isEmpty()) {
            System.err.println("Cannot write to Excel: CURRENT_TCID not set in ScenarioContext.");
            return;
        }

        String sheetName = ScenarioContext.getCurrentSheetName();
        if (sheetName == null) {
            System.err.println("Cannot write to Excel: Sheet not found for TestCaseID " + testCaseId);
            return;
        }

        // Track write iteration count separately from read iteration
        int iteration = com.automation.utils.ScenarioContext.getNextIteration(testCaseId + "_writeIter");

        writeIterationData(sheetName, testCaseId, iteration, dataToWrite);
    }
}
