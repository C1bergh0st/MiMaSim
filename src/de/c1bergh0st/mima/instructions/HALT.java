package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class HALT extends  Instruction{

    public HALT() {
        super("HALT", "HALT", false, true, 0b0000, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        mima.shouldHalt = true;
        Debug.send("SHOULDHALT");
    }
}
