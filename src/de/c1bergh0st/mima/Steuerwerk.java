//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.instructions.*;
import de.c1bergh0st.visual.DialogUtil;
import de.c1bergh0st.visual.ParseUtil;

public class Steuerwerk {

    //extCommandSet as specified here: http://ti.ira.uka.de/Visualisierungen/Mima/mima-aufgaben.pdf
    public static final String[] commandSet = {"LDC","LDV","STV","ADD","AND","OR","XOR","EQL","JMP","JMN","LDIV","STIV","JMS","JIND","SKIP"};
    public static final String[] extCommandSet = {"HALT","NOT","RAR"};


    public static final int MAX_ADRESS = 1048575;
    public static final int MAX_VALUE = 16777215;

    private RegisterMaster registers;
    private InstructionMaster instructions;
    private Speicher speicher;
    private ALU alu;
    public boolean shouldHalt;
    public int lastExecutedAdress;
    public int lastChange;


    public Steuerwerk(InstructionMaster instructions){
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
        this.instructions = instructions;
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
        int command = registers.get("ir").getValue();

        //increment the iar by one
        //TODO: use ALU
        registers.get("iar").setValue(registers.get("iar").getValue()+1);

        execInstr(command);

    }

    private void execInstr(int command){
        instructions.getInstr(command).exec(registers, speicher,this, command);
    }


    public void step(){
        lastExecutedAdress = registers.get("iar").getValue();
        //load the next instruction from memory into the ir
        registers.get("sar").setValue(registers.get("iar").getValue());
        speicher.updateSDR();
        registers.get("ir").setValue(registers.get("sdr").getValue());

        //get the OpCode
        int opCode = registers.get("ir").getValue();
        int command = registers.get("ir").getValue();
        byte opByte = registers.get("ir").getCommand();
        Debug.send(registers.get("iar").getValue()+" : "+ ParseUtil.code(opCode));
        //increment the iar by one
        //TODO: use ALU
        registers.get("iar").setValue(registers.get("iar").getValue()+1);

        execInstr(command);
        if(shouldHalt){
            DialogUtil.showDialogToUser("MiMa Halt","A HALT Statement has been reached!");
            shouldHalt = false;
        }
        Debug.send("Akku:"+registers.get("akku").getValue());
    }


    public void updateLastEdit(){
        lastChange = registers.get("sar").getValue();
    }

    public ALU getALU(){
        return alu;
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
