package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class LDIV extends  Instruction{

    public LDIV() {
        super("LDIV", "LDIV", true, false, 0b1010, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //get <ir> into the sdr
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //get <<ir>> into the sdr
        registers.get("sar").setValue(registers.get("sdr").getMaskedValue());
        speicher.updateSDR();
        //save the value of <<ir>> in the akku
        registers.get("akku").setValue(registers.get("sdr").getValue());
    }
}
