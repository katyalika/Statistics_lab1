package com.statistics.model;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.util.Map;
/**
 *
 * @author lihac
 */
public class XLSXExporter {
    public void exportStatistics(String filePath, Map<String, Object> stats) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Statistics");
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("0.######"));
            
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Statistic");
            headerRow.createCell(1).setCellValue("Value");
            headerRow.setRowStyle(headerStyle);
            
            int rowNum = 1;
            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                
                Cell valueCell = row.createCell(1);
                if (entry.getValue() instanceof Number) {
                    valueCell.setCellValue(((Number) entry.getValue()).doubleValue());
                    valueCell.setCellStyle(numberStyle);
                } else {
                    valueCell.setCellValue(entry.getValue().toString());
                }
            }
           
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }
}