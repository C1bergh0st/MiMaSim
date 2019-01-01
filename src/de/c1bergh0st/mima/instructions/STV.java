package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class STV extends Instruction {

    public STV() {
        super("STV", "STV", true, false, 0b0010, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        registers.get("sdr").setValue(registers.get("akku").getValue());
        speicher.write();
        mima.updateLastEdit();
    }
}
