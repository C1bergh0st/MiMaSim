//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.main;

import de.c1bergh0st.gui.InputController;
import de.c1bergh0st.gui.MainView;
import de.c1bergh0st.gui.ViewController;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //Bad Habit but no one cares if this fails its just E S T H E T I C S
        }
        JFrame frame = new JFrame("MiMa_II");
        ViewController viewController = new ViewController();
        MainView view = new MainView(new InputController(viewController));
        frame.add(view);
        viewController.setMainView(view);
        viewController.setFrame(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1600,900));
        frame.setVisible(true);
    }
}
