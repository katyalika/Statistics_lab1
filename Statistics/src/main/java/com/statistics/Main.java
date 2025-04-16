package com.statistics;

import com.statistics.controller.Controller;
import com.statistics.model.Model;
import com.statistics.view.View;
import javax.swing.SwingUtilities;

/**
 *
 * @author lihac
 */
public class Main {

    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model, view);
        view.showMainWindow();
    }
}
