package com.statistics.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author lihac
 */
public class View {
    private final JFrame frame;
    private final JButton importButton;
    private final JButton exportButton;
    private final JButton exitButton;
    private final JTextArea statisticsArea;
    private final JFileChooser fileChooser;

    public View() {
        frame = new JFrame("Statistics Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        importButton = new JButton("Import XLSX");
        exportButton = new JButton("Export Results");
        exitButton = new JButton("Exit");
        
        statisticsArea = new JTextArea();
        statisticsArea.setEditable(false);
        
        fileChooser = new JFileChooser();
        
        createUI();
    }
    
    private void createUI() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(exitButton);
        
        JScrollPane scrollPane = new JScrollPane(statisticsArea);
        
        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    public void showMainWindow() {
        frame.setVisible(true);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateStatisticsDisplay(Map<String, Object> statistics) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, Object> entry : statistics.entrySet()) {
        String value = entry.getValue().toString();
        if (value.equals("NaN")) {
            value = "N/A (недопустимые значения для расчета)";
        }
        sb.append(entry.getKey()).append(": ").append(value).append("\n");
    }
    statisticsArea.setText(sb.toString());
}

    public String showFileChooser(String mode) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);
    
        int returnValue;
        if ("Import".equals(mode)) {
         returnValue = fileChooser.showOpenDialog(frame);
        } else {
            returnValue = fileChooser.showSaveDialog(frame);
        }
    
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public JButton getImportButton() {
        return importButton;
    }

    public JButton getExportButton() {
        return exportButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
    
}
