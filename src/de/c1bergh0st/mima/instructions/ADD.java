package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class ADD extends  Instruction{

    public ADD() {
        super("ADD", "ADD", true, false, 0b0011, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //load the value at <ir> into the sdr
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save the value of the sdr into y
        registers.get("y").setValue(registers.get("sdr").getValue());
        //save the value of the akku into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //let the alu add the two together
        mima.getALU().add();
        //save the output into the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }
}
