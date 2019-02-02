package de.c1bergh0st.mima.executing.visual;

import de.c1bergh0st.mima.Steuerwerk;

import javax.swing.*;

public class MemoryViewer extends JFrame {


    public MemoryViewer(Steuerwerk mima){
        super("Full Memory");
        this.add(new MemoryEditor(mima.getSpeicher(),mima,1048576).getPanel());
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
