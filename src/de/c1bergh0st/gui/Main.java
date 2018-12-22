package de.c1bergh0st.gui;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Event;

import javax.swing.JFrame;

public class Main {
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        MainView view = new MainView();
        frame.add(view);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1000,600));
        frame.setVisible(true);
        view.editor.setText("This is a demonstration of...\n...line numbering using a JText area within...\n...a JScrollPane");;
    }

}
