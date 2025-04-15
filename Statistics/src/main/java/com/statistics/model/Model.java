package com.statistics.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lihac
 */

public class Model {
    private List<List<Double>> data;
    private List<String> columnNames;
    private Map<String, Object> statistics;
    private final XLSXParser xlsxParser;
    private final StatsCalculator statsCalculator;
    private final XLSXExporter xlsxExporter;

    public Model() {
        this.xlsxParser = new XLSXParser();
        this.statsCalculator = new StatsCalculator();
        this.xlsxExporter = new XLSXExporter();
    }

    public void importData(String filePath) throws Exception {
        this.data = xlsxParser.parseFile(filePath);
        this.columnNames = xlsxParser.getColumnNames();
    }

    public void calculateStatistics() {
        if (data == null) {
            throw new IllegalStateException("Data cannot be null");
        }
        if (data.isEmpty()) {
            throw new IllegalStateException("No data available for calculations");
        }
        List<double[]> numbersData = new ArrayList<>();
        for (List<Double> list : data) {
            double[] array = new double[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }
            numbersData.add(array);
        }
        this.statistics = statsCalculator.calculateAllStatistics(numbersData, columnNames);
    }

    public void exportData(String filePath) throws Exception {
        if (statistics == null || statistics.isEmpty()) {
            throw new IllegalStateException("No statistics available for export");
        }
        xlsxExporter.exportStatistics(filePath, statistics);
    }

    public Map<String, Object> getStatistics() {
        return statistics;
    }

    public List<List<Double>> getData() {
        return data;
    }
    
    public List<String> getColumnNames() {
        return columnNames;
    }
}
