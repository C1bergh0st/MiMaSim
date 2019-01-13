package de.c1bergh0st.mima.instructions;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.parsing.MiMaParsingException;
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

    public int parseSimpleLine(String line){
        String code = ParseUtil.getFirstWord(line);
        int result;
        //Input Sanitisation in new Scope
        {
            //TODO OPTIMIZE
            LinkedList<String> commands = getFullCommandList();
            //check if we know the command
            boolean known = false;
            for(String command : commands){
                if(!code.equals(command)){
                    known = true;
                }
            }
            if(!known){
                throw new IllegalArgumentException("No known Command found");
            }
        }
        //End of Input Sanitisation, Command exists
        Instruction instruction = getInstrByCommand(code);
        int argRes;

        //if the instruction takes args we parse that into the argRes argument
        if(instruction.takesArgs()){
            //If the Instruction takes Arguments but no Arguments are given an Exception is thrown
            if(!line.matches("[A-Z]+ [0-9]+")){
                throw new IllegalArgumentException("Malformed Statement, " + instruction.getSimpleCode() + " takes Arguments");
            }
            String arg = line.split(" ")[1];
            argRes = ParseUtil.mask20(Integer.parseInt(arg));
            if(instruction.isExtended()){
                argRes = argRes & 0b000000001111111111111111;
            }
        } else{
            argRes = 0;
        }

        //merges Argres and OpCode
        if(instruction.isExtended()){
            result = 0b111100000000000000000000 + ParseUtil.mask20(instruction.getOpCode()<<16);
            result += 0b000000001111111111111111 & argRes;
        } else{
            result = (instruction.getOpCode() << 20) +ParseUtil.mask20(argRes) ;
        }
        return ParseUtil.mask24(result);
    }

    private Instruction getInstrByCommand(String command){
        for(Instruction i : instructions){
            if(i.getSimpleCode().equals(command)){
                return i;
            }
        }
        for(Instruction i : extendedInstructions){
            if(i.getSimpleCode().equals(command)){
                return i;
            }
        }
        throw new AssertionError("Internal Error in InstructionMaster with: \"" + command + "\"");
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

    public LinkedList<String> getExtCommandList(){
        LinkedList<String> result = new LinkedList<>();
        addExtCommandList(result);
        return result;
    }

    public LinkedList<String> getCommandList(){
        LinkedList<String> result = new LinkedList<>();
        addCommandList(result);
        return result;
    }

    private void addCommandList(LinkedList<String> input){
        String regex;
        for(Instruction i : instructions){
            regex = i.getRegex();
            if(!input.contains(regex)){
                input.add(i.getRegex());
            }
        }
    }

    private void addExtCommandList(LinkedList<String> input){
        String regex;
        for(Instruction i : extendedInstructions){
            regex = i.getRegex();
            if(!input.contains(regex)){
                input.add(i.getRegex());
            }
        }
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
        addCommandList(result);
        addExtCommandList(result);
        return result;
    }

    public int getSkipCode(){
        if(!getFullCommandList().contains("SKIP")){
            throw new AssertionError("There is supposed to be at least one SKIP command in the InstructionMaster");
        }
        for(Instruction i : instructions){
            if(i.getSimpleCode().equals("SKIP")){
                return i.getOpCode()<<4;
            }
        }

        for(Instruction i : extendedInstructions){
            if(i.getSimpleCode().equals("SKIP")){
                return i.getOpCode();
            }
        }

        return 0;
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

