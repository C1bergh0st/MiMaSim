package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class NOT extends  Instruction{

    public NOT() {
        super("NOT", "NOT", false, true, 0b0001, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to invert
        mima.getALU().not();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }
}