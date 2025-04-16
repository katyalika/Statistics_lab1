package com.statistics.controller;

import com.statistics.model.Model;
import com.statistics.view.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author lihac
 */
public class Controller {
    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        JButton importButton = view.getImportButton();
        JButton exportButton = view.getExportButton();
        JButton exitButton = view.getExitButton();

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onImportButtonClick();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExportButtonClick();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExitButtonClick();
            }
        });
    }
    
    private void onImportButtonClick() {
    try {
        String filePath = view.showFileChooser("Import");
        if (filePath != null) {
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                view.showError("Choose XLSX file");
                return;
            }
            
            model.importData(filePath);
            model.calculateStatistics();
            view.updateStatisticsDisplay(model.getStatistics());
        }
    } catch (Exception ex) {
        handleError("Import error: " + ex.getMessage());
    }
}

    private void onExportButtonClick() {
        try {
            String filePath = view.showFileChooser("Export");
            if (filePath != null) {
                model.exportData(filePath);
                view.showMessage("Export completed successfully");
            }
        } catch (Exception ex) {
            handleError("Export error: " + ex.getMessage());
        }
    }
    
    private void onExitButtonClick() {
        System.exit(0);
    }

    private void handleError(String message) {
        if (message.contains(".xlsx")) {
            view.showError("Choose XLSX file");
        } else {
            view.showError("Error");
        }
    }
}
