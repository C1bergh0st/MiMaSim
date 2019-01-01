package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public class JMN extends  Instruction{

    public JMN() {
        super("JMN", "JMN", true, false, 0b1001, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        //if the first bit is negative we jump
        if(registers.get("akku").getFirstBit()){
            registers.get("iar").setValue(registers.get("ir").getMaskedValue());
        }
    }
}
