//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima.executing.visual;

import de.c1bergh0st.mima.Steuerwerk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BottomBar extends JPanel {
    private final JButton oneStep;
    private final JButton start;
    private final Steuerwerk mima;
    private final MemoryEditor memEdit;
    private final RegisterView registerView;


    public BottomBar(Steuerwerk mima, MemoryEditor memEdit, RegisterView registerView){
        this.mima = mima;
        this.memEdit = memEdit;
        this.registerView = registerView;
        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        oneStep = new JButton("One Step");
        oneStep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mima.step();
                memEdit.revalidate();
                registerView.refresh();
            }
        } );
        add(oneStep);

        start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mima.getSpeicher().lockCurrState();
                memEdit.revalidate();
                mima.stepTill(0xFFFFFFF);
                memEdit.revalidate();
                registerView.refresh();
            }
        } );
        add(start);
    }

}
