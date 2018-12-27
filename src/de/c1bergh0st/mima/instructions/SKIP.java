package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class SKIP extends Instruction{

    public SKIP(byte opCode, boolean extCode) {
        super("SKIP", "SKIP", false, extCode, opCode, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //Intentionally left blank as SKIP does nothing
    }
}
