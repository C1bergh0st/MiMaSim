//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima.executing.visual;

import de.c1bergh0st.mima.Steuerwerk;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JPanel {
    private final Steuerwerk mima;
    private final VisualRegister akku;
    private final VisualRegister ir;
    private final VisualRegister iar;


    public RegisterView(Steuerwerk mima){
        this.mima = mima;
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        akku = new VisualRegister(mima.getAkku(),"Akku",VisualRegister.FULLVALUE);
        add(akku);
        iar = new VisualRegister(mima.getIAR(),"IAR (Next step)",VisualRegister.ADRESS);
        add(iar);
        ir = new VisualRegister(mima.getIR(),"IR",VisualRegister.INSTRUCTION);
        add(ir);
        refresh();
    }

    public void refresh(){
        akku.refresh();
        ir.refresh();
        iar.refresh();
    }

}
