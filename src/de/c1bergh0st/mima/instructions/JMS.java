package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.visual.ParseUtil;

public class JMS extends Instruction{

    protected JMS() {
        super("JMS", "JMS", true, false, 0b1100, 1);
    }

    @Override
    public void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand) {
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        registers.get("sdr").setValue(registers.get("iar").getValue());
        speicher.write();
        mima.updateLastEdit();
        int next = ParseUtil.mask20(registers.get("ir").getMaskedValue() + 1);
        registers.get("iar").setValue(next);
    }
}
