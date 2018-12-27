package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.mima.RegisterMaster;
import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.visual.ParseUtil;

import java.net.PasswordAuthentication;

public class InstructionMaster {
    private Instruction[] instructions;
    private Instruction[] extendedInstructions;

    public InstructionMaster(){
        instructions = new Instruction[15];
        extendedInstructions = new Instruction[16];
    }

    public Instruction getInstr(int command){
        byte opCode = (byte)(ParseUtil.mask24(command)>>>20);
        if(opCode == 15){
            byte extOpCode = (byte)(ParseUtil.mask20(command)>>>16);
            return extendedInstructions[extOpCode];
        } else {
            return instructions[opCode];
        }
    }

    public void add(Instruction instr){
        if(instr.isExtended()){
            internalAddExt(instr);
        } else {
            internalAdd(instr);
        }
    }

    private void internalAddExt(Instruction instr){
        if(instr.getOpCode() < 0 || instr.getOpCode() > 15){
            throw new IllegalStateException("extOpcode of "+instr+" is invalid");
        } else {
            extendedInstructions[instr.getOpCode()] = instr;
        }
    }

    private void internalAdd(Instruction instr){
        if(instr.getOpCode() < 0 || instr.getOpCode() > 14){
            throw new IllegalStateException("Opcode of "+instr+" is invalid");
        } else {
            instructions[instr.getOpCode()] = instr;
        }
    }
}
