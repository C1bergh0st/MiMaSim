package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;

public abstract class Instruction {

    private String simpleCode;
    private String regex;
    private boolean takesArgs;
    private boolean exOpCode;
    private byte opCode;

    public String getSimpleCode(){
        return simpleCode;
    }

    public byte getOpCode(){
        return opCode;
    }

    public boolean isExtended(){
        return exOpCode;
    }

    public boolean takesArgs(){
        return takesArgs;
    }

    public abstract void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int args);

    public String getRegex(){
        return regex;
    }

}
