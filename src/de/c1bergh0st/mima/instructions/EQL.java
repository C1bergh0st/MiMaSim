package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class EQL extends  Instruction {

    public EQL() {
        super("EQL", "EQL", true, false, 0b0111, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //get the value at <ir>
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        registers.get("y").setValue(registers.get("sdr").getValue());
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to compare
        mima.getALU().eql();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }
}