//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.main;

import de.c1bergh0st.gui.MainView;
import de.c1bergh0st.mima.Steuerwerk;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        Steuerwerk mima = new Steuerwerk();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //Bad Habit but no one cares if this fails its just E S T H E T I C S
        }
        JFrame frame = new JFrame("MiMa_II");
        frame.add(new MainView());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1000,600));
        frame.setVisible(true);

    }
}