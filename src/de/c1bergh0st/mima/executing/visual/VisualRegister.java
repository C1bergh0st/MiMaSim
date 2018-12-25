//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima.executing.visual;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.Register;
import de.c1bergh0st.mima.parsing.DataConverter;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("WeakerAccess")
public class VisualRegister extends JPanel {
    public static final int FULLVALUE = 0;
    public static final int ADRESS = 1;
    public static final int INSTRUCTION = 2;
    private Register register;
    private JLabel registerName;
    private JLabel binary;
    private JLabel meaning;
    private int type;

    public VisualRegister(Register register, String name, int type){
        super();
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        int height = 0;
        switch (type){
            case FULLVALUE:
                height = 60;
                break;
            case ADRESS:
                height = 40;
                break;
            case INSTRUCTION:
                height = 40;
                break;
        }
        this.setPreferredSize(new Dimension(300, height));
        this.register = register;
        this.type = type;
        registerName = new JLabel(" "+name);
        registerName.setBackground(Color.ORANGE);
        add(registerName);
        meaning = new JLabel("PLACEHOLDER");
        add(meaning);
        binary = new JLabel(DataConverter.getBinary(1,24));
        if(type != INSTRUCTION && type != ADRESS){
            add(binary);
        }
    }

    public void refresh(){
        int i = register.getValue();
        if(type == FULLVALUE){ //Only the Decimal Representation
            String decimal = " " + DataConverter.getSignRepr(i);
            meaning.setText(decimal);
        } else if(type == ADRESS){
            String decimal = " " + DataConverter.getSignRepr(i);
            meaning.setText(decimal);
        } else if(type == INSTRUCTION){
            String decimal = DataConverter.getCodeRepr(i);
            meaning.setText(" "+decimal);
        } else{
            Debug.sendErr("Wrong type in VisualRegister",1);
        }
        String binary = DataConverter.getBinary(i,24);
        this.binary.setText(" "+binary);
    }
}
