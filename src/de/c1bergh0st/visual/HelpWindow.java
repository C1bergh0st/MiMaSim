//Copyright (C) 2018  Philipp Berdesinski

package de.c1bergh0st.visual;

import javax.swing.*;
import java.awt.*;

public class HelpWindow extends JFrame {
    private String info = "<html><center>MiMa_II<br><br>" +
            "A MiMa Simulator with built-in Editor<br><br>" +
            "made by Philipp (c1bergh0st) Berdesinski<br>" +
            "© 2019 Philipp Berdesinski<br><br>" +
            "Use at own risk<br>" +
            "The Author will not be liable to any damages created through use of this software</center></html>";
    public HelpWindow(){
        super("HELP");
        JPanel panel = new JPanel();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //panel.setPreferredSize(new Dimension(300,200));
        panel.add(new JLabel(info));
        this.add(panel);
        this.setPreferredSize(new Dimension(550,200));
        this.pack();
        this.setVisible(true);
    }
}
