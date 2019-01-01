package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class JMP extends  Instruction{

    public JMP() {
        super("JMP", "JMP", true, false, 0b1000, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        registers.get("iar").setValue(registers.get("ir").getMaskedValue());
    }
}
