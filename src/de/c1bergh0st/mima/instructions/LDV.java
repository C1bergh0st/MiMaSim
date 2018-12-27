package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class LDV extends Instruction{

    public LDV() {
        super("LDV", "LDV", true, false, 0b0001, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //write into the sar
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        //tell the memory to update the sdr
        speicher.updateSDR();
        //save the sdr value in the akku
        registers.get("akku").setValue(registers.get("sdr").getValue());
    }
}
