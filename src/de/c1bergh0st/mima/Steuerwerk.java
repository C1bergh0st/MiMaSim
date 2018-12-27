//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.instructions.InstructionMaster;
import de.c1bergh0st.mima.instructions.LDC;
import de.c1bergh0st.visual.DialogUtil;
import de.c1bergh0st.visual.ParseUtil;

public class Steuerwerk {

    //extCommandSet as specified here: http://ti.ira.uka.de/Visualisierungen/Mima/mima-aufgaben.pdf
    public static final String[] commandSet = {"LDC","LDV","STV","ADD","AND","OR","XOR","EQL","JMP","JMN","LDIV","STIV","SKIP","SKIP","SKIP"};
    public static final String[] extCommandSet = {"HALT","NOT","RAR"};


    public static final int MAX_ADRESS = 1048575;
    public static final int MAX_VALUE = 16777215;

    private RegisterMaster registers;
    private InstructionMaster instructions;
    private Speicher speicher;
    private ALU alu;
    private boolean shouldHalt;
    public int lastExecutedAdress;
    public int lastChange;


    public Steuerwerk(){
        registers = new RegisterMaster();
        registers.addRegister("eins", new Register(true,1,24));
        registers.addRegister("akku",new Register());
        registers.addRegister("sar",new Register(false, 0, 20));
        registers.addRegister("sdr",new Register());
        speicher = new Speicher(registers.get("sdr"),registers.get("sar"));
        registers.addRegister("iar", new Register(false, 0, 20));
        registers.addRegister("ir",new Register());
        registers.addRegister("x", new Register());
        registers.addRegister("y", new Register());
        registers.addRegister("z", new Register());
        instructions = new InstructionMaster();
        instructions.add(new LDC());
        alu = new ALU(registers.get("x"),registers.get("y"),registers.get("z"));
        shouldHalt = false;
        lastExecutedAdress = 0;
        lastChange = 0;


        //akku.setValue(0b111111111111111111111111);
        //speicher.setMem(0,0b000000000000000000001111);
    }

    public void stepTill(int maxsteps){

        //Reset the IAR to 0
        resetAdress();
        shouldHalt = false;
        //Execute Steps untill a Halt or untill maxsteps have been executed
        int steps = 0;
        while(!shouldHalt && steps < maxsteps){
            lightstep();
            steps++;
        }
        if(steps == maxsteps){
            DialogUtil.showErrorToUser("Error","After "+steps+" Steps, no HALT Command was found");
        } else{
            DialogUtil.showDialogToUser("Success","Execution reached Halt on Step "+steps+" !");
        }
    }

    public void reset(){
        resetAdress();
        registers.get("akku").setValue(0);
        registers.get("sdr").setValue(0);
        registers.get("sar").setValue(0);
        registers.get("ir").setValue(0);
        registers.get("x").setValue(0);
        registers.get("y").setValue(0);
        registers.get("z").setValue(0);
        speicher.loadLockedState();
        shouldHalt = false;
    }

    public void resetAdress(){
        registers.get("iar").setValue(0);
        lastExecutedAdress = 0;
        lastChange = 0;
    }

    public void lightstep(){
        lastExecutedAdress = registers.get("iar").getValue();
        //load the next instruction from memory into the ir
        registers.get("sar").setValue(registers.get("iar").getValue());
        speicher.updateSDR();
        registers.get("ir").setValue(registers.get("sdr").getValue());

        //get the OpCode
        byte opCode = registers.get("ir").getCommand();

        //increment the iar by one
        //TODO: use ALU
        registers.get("iar").setValue(registers.get("iar").getValue()+1);

        execInstr(opCode);

    }

    private void execInstr(byte b){
        int command = registers.getValue("ir");
        switch (b){
            case 0: //LDC
                //ldc();
                instructions.getInstr(command).exec(registers, speicher,this, command);
                break;
            case 1: //LDV
                ldv();
                break;
            case 2: //STV
                stv();
                break;
            case 3: //ADD
                add();
                break;
            case 4: //AND
                and();
                break;
            case 5: //OR
                or();
                break;
            case 6: //XOR
                xor();
                break;
            case 7: //EQL
                eql();
                break;
            case 8: //JMP
                jmp();
                break;
            case 9: //JMN
                jmn();
                break;
            case 10: //LDIV
                ldiv();
                break;
            case 11: //STIV
                stiv();
                break;
            case 12: //SKIP

                break;
            case 13: //SKIP

                break;
            case 14: //EMPTY

                break;
            case 15: //EXTCODE
                execExtInstr();
                break;
            default: //EMPTY
                Debug.sendErr("INSTRUCTION > 0B1111",2);
                break;
        }
    }

    private void execExtInstr(){
        byte nextByte = (byte)(registers.get("ir").getMaskedValue()>>>16);
        switch (nextByte){
            case 0:
                halt();
                break;
            case 1:
                not();
                break;
            case 2:
                rar();
                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;
            case 10:

                break;
            case 11:

                break;
            case 12:

                break;
            case 13:

                break;
            case 14:

                break;
            case 15:

                break;
            default:
                Debug.sendErr("Internal Error in Extended opCode Execution in Steuerwerk",0);
                break;
        }
    }

    public void step(){
        lastExecutedAdress = registers.get("iar").getValue();
        //load the next instruction from memory into the ir
        registers.get("sar").setValue(registers.get("iar").getValue());
        speicher.updateSDR();
        registers.get("ir").setValue(registers.get("sdr").getValue());

        //get the OpCode
        int opCode = registers.get("ir").getValue();
        byte opByte = registers.get("ir").getCommand();
        Debug.send(registers.get("iar").getValue()+" : "+ ParseUtil.code(opCode));
        //increment the iar by one
        //TODO: use ALU
        registers.get("iar").setValue(registers.get("iar").getValue()+1);

        execInstr(opByte);
        if(shouldHalt){
            DialogUtil.showDialogToUser("MiMa Halt","A HALT Statement has been reached!");
            shouldHalt = false;
        }
        Debug.send("Akku:"+registers.get("akku").getValue());
    }

    private void ldc(){
        //load the value of the ir without the command halfbyte into the akku
        registers.get("akku").setValue(registers.get("ir").getMaskedValue());
    }

    private void ldv(){
        //write into the sar
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        //tell the memory to update the sdr
        speicher.updateSDR();
        //save the sdr value in the akku
        registers.get("akku").setValue(registers.get("sdr").getValue());
    }

    private void stv(){
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        registers.get("sdr").setValue(registers.get("akku").getValue());
        speicher.write();
        lastChange = registers.get("sar").getValue();
    }

    private void add(){
        //load the value at <ir> into the sdr
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save the value of the sdr into y
        registers.get("y").setValue(registers.get("sdr").getValue());
        //save the value of the akku into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //let the alu add the two together
        alu.add();
        //save the output into the akku
        registers.get("akku").setValue(registers.get("z").getValue());

    }

    private void and(){
        //get the value at <ir>
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        registers.get("y").setValue(registers.get("sdr").getValue());
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to and
        alu.and();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }

    private void or(){
        //get the value at <ir>
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        registers.get("y").setValue(registers.get("sdr").getValue());
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to or
        alu.or();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }

    private void xor(){
        //get the value at <ir>
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        registers.get("y").setValue(registers.get("sdr").getValue());
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to xor
        alu.xor();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }

    private void eql(){
        //get the value at <ir>
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        registers.get("y").setValue(registers.get("sdr").getValue());
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to compare
        alu.eql();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }

    private void jmp(){
        registers.get("iar").setValue(registers.get("ir").getMaskedValue());
    }

    private void jmn(){
        //if the first bit is negative we jump
        if(registers.get("akku").getFirstBit()){
            jmp();
        }
    }

    private void ldiv(){
        //get <ir> into the sdr
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //get <<ir>> into the sdr
        registers.get("sar").setValue(registers.get("sdr").getMaskedValue());
        speicher.updateSDR();
        //save the value of <<ir>> in the akku
        registers.get("akku").setValue(registers.get("sdr").getValue());
    }

    private void stiv(){
        //get <ir>
        registers.get("sar").setValue(registers.get("ir").getMaskedValue());
        speicher.updateSDR();
        //save <<ir>> in the sar
        registers.get("sar").setValue(registers.get("sdr").getValue());
        //save <akku> in the sdr
        registers.get("sdr").setValue(registers.get("akku").getValue());
        //save <akku> at <<ir>>
        speicher.write();
        lastChange = registers.get("sar").getValue();
    }

    private void rar(){
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to rotate right (and not loose the lost bit)
        alu.rar();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }

    private void not(){
        //get <akku> into x
        registers.get("x").setValue(registers.get("akku").getValue());
        //tell the alu to invert
        alu.not();
        //save the output in the akku
        registers.get("akku").setValue(registers.get("z").getValue());
    }

    private void halt(){
        shouldHalt = true;
        Debug.send("SHOULDHALT");
    }

    public Speicher getSpeicher(){
        return speicher;
    }

    public Register getAkku(){
        return registers.get("akku");
    }

    public Register getIAR() {
        return registers.get("iar");
    }

    public Register getIR() {
        return registers.get("ir");
    }

    public int getNextAdress(){
        return registers.get("iar").getValue();
    }
    public int getLastAdress(){
        return lastExecutedAdress;
    }
    public int getLastChange(){
        return lastChange;
    }
}
