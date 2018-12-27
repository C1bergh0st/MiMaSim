package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class LDC extends  Instruction{

    public LDC() {
        super("LDC", "LDC", true, false, 0b0000, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //load the value of the ir without the command halfbyte into the akku
        registers.get("akku").setValue(registers.get("ir").getMaskedValue());
    }
}
