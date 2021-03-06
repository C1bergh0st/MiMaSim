//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.main;

import de.c1bergh0st.config.Colors;
import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.gui.InputController;
import de.c1bergh0st.gui.MainView;
import de.c1bergh0st.gui.ViewController;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        /*try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
            Debug.send("L&F loaded sucessfully");
        } catch (Exception e) {
            e.printStackTrace();
            Colors.DEFAULT_COLOR = Color.BLACK;
            Colors.EDITOR_DISABLED = new Color(255,200,200);
            Colors.EDITOR_ENABLED = Color.WHITE;
            //Bad Habit but no one cares if this fails its just E S T H E T I C S
        }*/
        Colors.DEFAULT_COLOR = Color.BLACK;
        Colors.EDITOR_DISABLED = new Color(255,200,200);
        Colors.EDITOR_ENABLED = Color.WHITE;
        Colors.MIMA_TABLE_DEFAULT = Color.WHITE;


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
