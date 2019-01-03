//Copyright (C) 2018  Philipp Berdesinski

package de.c1bergh0st.visual;

import javax.swing.*;

public class HelpWindow extends JFrame {
    private String info = "TODO: IMPLEMENT";
    public HelpWindow(){
        super("HELP");
        JPanel panel = new JPanel();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //panel.setPreferredSize(new Dimension(300,200));
        panel.add(new JLabel(info));
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}
