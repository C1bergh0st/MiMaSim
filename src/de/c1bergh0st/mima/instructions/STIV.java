package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class STIV extends  Instruction{

    public STIV() {
        super("STIV", "STIV", true, false, 0b1011, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //get <ir>
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save <<ir>> in the sar
        registers.get("sar").setValue(registers.get("sdr").getValue());
        //save <akku> in the sdr
        registers.get("sdr").setValue(registers.get("akku").getValue());
        //save <akku> at <<ir>>
        speicher.write();
        mima.updateLastEdit();
    }
}
