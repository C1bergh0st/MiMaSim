//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima.executing.visual;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.Steuerwerk;

import javax.swing.*;
import java.awt.*;

public class BottomBar extends JPanel {
    private final JButton oneStep;
    private final JButton start;
    private final JButton reset;
    private final JButton showMem;
    private final Steuerwerk mima;
    private final MemoryEditor memEdit;
    private final RegisterView registerView;

    private long last;


    public BottomBar(Steuerwerk mima, MemoryEditor memEdit, RegisterView registerView){
        this.mima = mima;
        this.memEdit = memEdit;
        this.registerView = registerView;
        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        last = 0;

        oneStep = new JButton("Step");
        oneStep.addActionListener(e -> {
            mima.step();
            memEdit.revalidate();
            registerView.refresh();
        });
        add(oneStep);

        start = new JButton("Start");
        start.addActionListener(e -> {
            mima.reset();
            memEdit.revalidate();
            mima.stepTill(0xFFFFFFF);
            memEdit.revalidate();
            registerView.refresh();
        });
        add(start);

        showMem = new JButton("Full Memory Snapshot");
        showMem.addActionListener(e -> {
            //this is needed because the user might open too many MemoryViewers on accident
            if(last < System.currentTimeMillis()){
                new MemoryViewer(mima);
                last = System.currentTimeMillis() + 500;
            }
        });
        add(showMem);

        reset = new JButton("Reset");
        reset.addActionListener(e -> {
            mima.reset();
            memEdit.revalidate();
            registerView.refresh();
        });
        add(reset);
        mima.getSpeicher().lockCurrState();
    }

}
