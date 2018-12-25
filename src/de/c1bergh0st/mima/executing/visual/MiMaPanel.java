package de.c1bergh0st.mima.executing.visual;

import de.c1bergh0st.mima.Steuerwerk;

import javax.swing.*;
import java.awt.*;

public class MiMaPanel extends JPanel {

    public MiMaPanel(Steuerwerk mima){
        super();
        System.out.println("Initializing MiMa Runtime GUI");
        MemoryEditor memEdit = new MemoryEditor(mima.getSpeicher(),mima);
        RegisterView regView = new RegisterView(mima);


        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600,600));
        setMaximumSize(new Dimension(600,2000));
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(memEdit.getPanel());
        split.setRightComponent(regView);
        //add(memEdit.getPanel(), BorderLayout.WEST);
        //add(regView, BorderLayout.EAST);
        add(split, BorderLayout.WEST);
        add(new BottomBar(mima, memEdit, regView), BorderLayout.PAGE_END);


    }


}
