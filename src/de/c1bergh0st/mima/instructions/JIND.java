package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class JIND extends Instruction{

    protected JIND() {
        super("JIND", "JIND", true, false, 0b1101, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //write into the sar
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        //tell the memory to update the sdr
        speicher.updateSDR();
        int target = registers.getValue("sdr");
        registers.get("iar").setValue(target);
    }
}
