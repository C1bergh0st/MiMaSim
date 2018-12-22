//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima;

import de.c1bergh0st.debug.Debug;

public class Register {
    private final boolean readOnly;
    private int value;
    private final int size;

    public Register(Boolean readOnly, int initialValue, int size){
        this.size = size;
        setValue(initialValue);
        this.readOnly = readOnly;
    }

    public Register(){
        this(false,0,24);
    }

    public int getValue(){
        return value;
    }

    public int getMaskedValue(){
        return value & 0b000011111111111111111111;
    }

    public int getSize(){
        return size;
    }

    public byte getCommand(){
        if(size == 24){
            return (byte)(value>>>20);
        } else {
            Debug.sendErr("This method is for length 24 Registers only",2);
            return 0;
        }
    }

    public boolean getFirstBit(){
        if(size == 24){
            return value >= 0b011111111111111111111111;
        } else {
            Debug.sendErr("Not Allowed");
            return false;
        }
    }

    public void setValue(int v){
        if(!readOnly){
            if(v < 0) {
                Debug.sendErr("Value " + v + " not Supported by Register", 2);
            } else {
                value = v & 0b111111111111111111111111;
            }
        } else {
            Debug.sendErr("The Register is READ-ONLY");
        }
    }

}
