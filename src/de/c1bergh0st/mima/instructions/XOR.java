package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class XOR extends  Instruction {

    public XOR() {
        super("XOR", "XOR", true, false, 0b0110, 1);
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
        //tell the alu to or
        mima.getALU().xor();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }
}
