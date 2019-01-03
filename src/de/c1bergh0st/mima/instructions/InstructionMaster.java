package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.visual.ParseUtil;

import java.util.LinkedList;

public class InstructionMaster {
    private Instruction[] instructions;
    private Instruction[] extendedInstructions;

    public InstructionMaster(){
        instructions = new Instruction[15];
        extendedInstructions = new Instruction[16];
        fillInstructionSet();
    }

    public void loadMiMa(){
        add(new LDC());
        add(new LDV());
        add(new STV());
        add(new ADD());
        add(new AND());
        add(new OR());
        add(new XOR());
        add(new EQL());
        add(new JMP());
        add(new JMN());
        add(new LDIV());
        add(new STIV());
        add(new HALT());
        add(new NOT());
        add(new RAR());
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
        Debug.sendRaw("Added Instruction: " + instr);
    }


    public LinkedList<String> getCommandList(boolean takesArgs){
        LinkedList<String> result = new LinkedList<>();
        String regex;
        for(Instruction i : instructions){
            regex = i.getRegex();
            if(!result.contains(regex) && (takesArgs == i.takesArgs())){
                result.add(i.getRegex());
            }
        }
        for(Instruction i : extendedInstructions){
            regex = i.getRegex();
            if(!result.contains(regex) && (takesArgs == i.takesArgs())){
                result.add(i.getRegex());
            }
        }
        return result;
    }

    public LinkedList<String> getFullCommandList(){
        LinkedList<String> result = new LinkedList<>();
        String regex;
        for(Instruction i : instructions){
            regex = i.getRegex();
            if(!result.contains(regex)){
                result.add(i.getRegex());
            }
        }
        for(Instruction i : extendedInstructions){
            regex = i.getRegex();
            if(!result.contains(regex)){
                result.add(i.getRegex());
            }
        }
        return result;
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

    private void fillInstructionSet(){
        for(byte i = 0; i < instructions.length; i++){
            instructions[i] = new SKIP(i,false);
            extendedInstructions[i] = new SKIP(i,true);
        }
        extendedInstructions[15] = new SKIP((byte)15,true);
    }

    public void printCommmandSet(){
        Debug.sendRaw(this.toString());
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Instruction instr : instructions){
            sb.append(instr.toString());
            sb.append("\n");
        }
        for(Instruction instr : extendedInstructions){
            sb.append(instr.toString());
            sb.append("\n");
        }
        sb.delete(sb.lastIndexOf("\n"),sb.length());
        return sb.toString();
    }


}

