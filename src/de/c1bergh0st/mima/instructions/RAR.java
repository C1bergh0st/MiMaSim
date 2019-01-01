package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class RAR extends  Instruction{

    public RAR() {
        super("RAR", "RAR", false, true, 0b0010, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to rotate right (and not loose the lost bit)
        mima.getALU().rar();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }
}
