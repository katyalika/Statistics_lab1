package com.statistics.model;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.*;

/**
 *
 * @author lihac
 */
public class XLSXParser {
    private List<String> columnNames = new ArrayList<>();
    private FormulaEvaluator evaluator;
    
    public List<List<Double>> parseFile(String filePath) throws Exception {
        if (!filePath.toLowerCase().endsWith(".xlsx")) {
        throw new IllegalArgumentException("File must have .xlsx extension");
        }
        List<List<Double>> result = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new IllegalArgumentException("Sheet is empty");
            }
            Row headerRow = sheet.getRow(0);
            int numColumns = headerRow.getLastCellNum();
            
            for (int i = 0; i < numColumns; i++) {
                Cell cell = headerRow.getCell(i);
                String name = (cell != null) ? getCellValueAsString(cell) : 
                              Character.toString((char) ('A' + i));
                columnNames.add(name);
            }
            
            for (int i = 0; i < numColumns; i++) {
                result.add(new ArrayList<>());
            }
            
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    for (int col = 0; col < numColumns; col++) {
                        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null) {
                            double value = getNumericCellValue(cell);
                            if (!Double.isNaN(value)) {
                                result.get(col).add(value);
                            }
                        }
                    }
                }
            }
        }
        
        validateData(result);
        return result;
    }
    
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case FORMULA:
                return getCellValueAsString(evaluator.evaluateInCell(cell));
            default:
                return Character.toString((char) ('A' + cell.getColumnIndex()));
        }
    }
    
    private double getNumericCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case FORMULA:
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case NUMERIC:
                        return cellValue.getNumberValue();
                    default:
                        return Double.NaN;
                }
            default:
                return Double.NaN;
        }
    }
    
    public List<String> getColumnNames() {
        return columnNames;
    }
    
    private void validateData(List<List<Double>> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("No valid data found in the file");
        }
        
        for (List<Double> column : data) {
            if (column.size() < 2) {
                throw new IllegalArgumentException("Not enough values for calculating");
            }
        }
    }
}