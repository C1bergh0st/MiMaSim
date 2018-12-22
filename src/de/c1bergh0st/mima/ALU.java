//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima;

import de.c1bergh0st.debug.Debug;

public class ALU {
    private Register x;
    private Register y;
    private Register z;

    public ALU(Register x, Register y, Register z){
        if(x != null && y != null && z != null){
            this.x = x;
            this.y = y;
            this.z = z;
        } else {
            Debug.sendErr("ALU REGISTER INVALID",2);
        }
    }


    public void add(){ //z = x + y
        z.setValue(x.getValue()+y.getValue());
    }

    public void and(){ //z = bitwise x and y
        z.setValue(Math.abs(x.getValue() & y.getValue()));
    }

    public void or(){ // z = bitwise x or y
        z.setValue(Math.abs(x.getValue() | y.getValue()));
    }

    public void xor(){ // z = bitwise x xor y
        z.setValue(Math.abs(x.getValue() ^ y.getValue()));
    }

    public void eql(){ // z = (x == y)
        if(x.getValue() == y.getValue()){
            z.setValue(0b111111111111111111111111);
        }
        else{
            z.setValue(0);
        }
    }

    public void not(){ // z = bitwise not x
        z.setValue(Math.abs(x.getValue() ^ 0b111111111111111111111111));
    }

    @SuppressWarnings("SpellCheckingInspection")
    public void rar(){ // z = rotate right x
        //if the last bit is 1 t is 1 else 0
        int t = x.getValue()%2;
        //shift 1 to the right with new zero
        int newz = x.getValue()>>>1;
        //add the lost bit at the beginning
        newz += t*Math.pow(2,23);
        //save z
        z.setValue(newz);
    }

}
