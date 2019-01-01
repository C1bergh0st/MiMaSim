package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.mima.parsing.DataConverter;

public abstract class Instruction {

    private static final String FOUR_BIT_PADDING = " ----";

    protected final String simpleCode;
    protected final String regex;
    protected final boolean takesArgs;
    protected final boolean exOpCode;
    protected final byte opCode;
    protected final int cycles;

    protected Instruction(String simpleCode, String regex, boolean takesArgs, boolean exOpCode, int opCode, int cycles) {
        this.simpleCode = simpleCode;
        this.regex = regex;
        this.takesArgs = takesArgs;
        this.exOpCode = exOpCode;
        this.opCode = (byte)opCode;
        this.cycles = cycles;
    }

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

    public abstract void exec(RegisterMaster registers, Speicher speicher, Steuerwerk mima, int fullCommand);

    public String getRegex(){
        return regex;
    }

    public int getCycles(){
        return cycles;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[Name: ");
        sb.append(String.format("%-5s",simpleCode));
        sb.append(" | Signature: ");
        if(exOpCode){
            sb.append("1111 ");
            sb.append(DataConverter.getBinary(opCode,4));
        } else{
            sb.append(DataConverter.getBinary(opCode,4));
            sb.append(FOUR_BIT_PADDING);
        }
        sb.append(FOUR_BIT_PADDING);
        sb.append(FOUR_BIT_PADDING);
        sb.append(FOUR_BIT_PADDING);
        sb.append(FOUR_BIT_PADDING);
        sb.append("]");
        return sb.toString();
    }

}
