//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.visual.DialogUtil;
import de.c1bergh0st.visual.ParseUtil;

public class Steuerwerk {
    public static final int MAX_ADRESS = 1048575;
    public static final int MAX_VALUE = 16777215;

    private Register akku;
    private Register sar, sdr;
    private Speicher speicher;
    private Register iar, ir;
    private Register eins;
    private ALU alu;
    private Register x, y, z;
    public boolean shouldHalt;
    int lastExecutedAdress;
    int lastChange;


    public Steuerwerk(){
        eins = new Register(true,1,24);
        akku = new Register();
        sar = new Register(false, 0, 20);
        sdr = new Register();
        speicher = new Speicher(sdr,sar);
        iar = new Register(false, 0, 20);
        ir = new Register();
        x = new Register();
        y = new Register();
        z = new Register();
        alu = new ALU(x,y,z);
        shouldHalt = false;
        lastExecutedAdress = 0;
        lastChange = 0;
        akku.setValue(0b111111111111111111111111);
        speicher.setMem(0,0b000000000000000000001111);
        speicher.setMem(1,0b000100000000000000000000);
        speicher.setMem(2,0b101100000000000000000000);
        speicher.setMem(3,0b000000000000000000000001);
        speicher.setMem(4,0b001100000000000000000000);
        speicher.setMem(5,0b001000000000000000000000);
        speicher.setMem(6,0b011100000000000000001010);
        speicher.setMem(7,0b110100000000000000000000);
        speicher.setMem(8,0b100100000000000000000001);
        speicher.setMem(9,0b111100000000000000000000);
        speicher.setMem(10,0b000000000000000000100000);
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

    public void resetAdress(){
        iar.setValue(0);
        lastExecutedAdress = 0;
        lastChange = 0;
    }

    public void lightstep(){
        lastExecutedAdress = iar.getValue();
        //load the next instruction from memory into the ir
        sar.setValue(iar.getValue());
        speicher.updateSDR();
        ir.setValue(sdr.getValue());

        //get the OpCode
        byte opCode = ir.getCommand();

        //increment the iar by one
        //TODO: use ALU
        iar.setValue(iar.getValue()+1);

        execInstr(opCode);

    }

    private void execInstr(byte b){
        switch (b){
            case 0: //LDC
                ldc();
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
            case 12: //RAR
                rar();
                break;
            case 13: //NOT
                not();
                break;
            case 14: //EMPTY

                break;
            case 15: //HALT
                halt();
                break;
            default: //EMPTY
                Debug.sendErr("INSTRUCTION > 0B1111",2);
                break;
        }
    }

    public void step(){
        lastExecutedAdress = iar.getValue();
        //load the next instruction from memory into the ir
        sar.setValue(iar.getValue());
        speicher.updateSDR();
        ir.setValue(sdr.getValue());

        //get the OpCode
        int opCode = ir.getValue();
        byte opByte = ir.getCommand();
        Debug.send(iar.getValue()+" : "+ ParseUtil.code(opCode));
        //increment the iar by one
        //TODO: use ALU
        iar.setValue(iar.getValue()+1);

        execInstr(opByte);

        Debug.send("Akku:"+akku.getValue());
    }

    private void ldc(){
        //load the value of the ir without the command halfbyte into the akku
        akku.setValue(ir.getMaskedValue());
    }

    private void ldv(){
        //write into the sar
        sar.setValue(ir.getMaskedValue());
        //tell the memory to update the sdr
        speicher.updateSDR();
        //save the sdr value in the akku
        akku.setValue(sdr.getValue());
    }

    private void stv(){
        sar.setValue(ir.getMaskedValue());
        sdr.setValue(akku.getValue());
        speicher.write();
        lastChange = sar.getValue();
    }

    private void add(){
        //load the value at <ir> into the sdr
        sar.setValue(ir.getMaskedValue());
        speicher.updateSDR();
        //save the value of the sdr into y
        y.setValue(sdr.getValue());
        //save the value of the akku into x
        x.setValue(akku.getValue());
        //let the alu add the two together
        alu.add();
        //save the output into the akku
        akku.setValue(z.getValue());

    }

    private void and(){
        //get the value at <ir>
        sar.setValue(ir.getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        y.setValue(sdr.getValue());
        //get <akku> into x
        x.setValue(akku.getValue());
        //tell the alu to and
        alu.and();
        //save the output in the akku
        akku.setValue(z.getValue());
    }

    private void or(){
        //get the value at <ir>
        sar.setValue(ir.getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        y.setValue(sdr.getValue());
        //get <akku> into x
        x.setValue(akku.getValue());
        //tell the alu to or
        alu.or();
        //save the output in the akku
        akku.setValue(z.getValue());
    }

    private void xor(){
        //get the value at <ir>
        sar.setValue(ir.getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        y.setValue(sdr.getValue());
        //get <akku> into x
        x.setValue(akku.getValue());
        //tell the alu to xor
        alu.xor();
        //save the output in the akku
        akku.setValue(z.getValue());
    }

    private void eql(){
        //get the value at <ir>
        sar.setValue(ir.getMaskedValue());
        speicher.updateSDR();
        //save <sdr> into y
        y.setValue(sdr.getValue());
        //get <akku> into x
        x.setValue(akku.getValue());
        //tell the alu to compare
        alu.eql();
        //save the output in the akku
        akku.setValue(z.getValue());
    }

    private void jmp(){
        iar.setValue(ir.getMaskedValue());
    }

    private void jmn(){
        //if the first bit is negative we jump
        if(akku.getFirstBit()){
            jmp();
        }
    }

    private void ldiv(){
        //get <ir> into the sdr
        sar.setValue(ir.getMaskedValue());
        speicher.updateSDR();
        //get <<ir>> into the sdr
        sar.setValue(sdr.getMaskedValue());
        speicher.updateSDR();
        //save the value of <<ir>> in the akku
        akku.setValue(sdr.getValue());
    }

    private void stiv(){
        //get <ir>
        sar.setValue(ir.getMaskedValue());
        speicher.updateSDR();
        //save <<ir>> in the sar
        sar.setValue(sdr.getValue());
        //save <akku> in the sdr
        sdr.setValue(akku.getValue());
        //save <akku> at <<ir>>
        speicher.write();
        lastChange = sar.getValue();
    }

    private void rar(){
        //get <akku> into x
        x.setValue(akku.getValue());
        //tell the alu to rotate right (and not loose the lost bit)
        alu.rar();
        //save the output in the akku
        akku.setValue(z.getValue());
    }

    private void not(){
        //get <akku> into x
        x.setValue(akku.getValue());
        //tell the alu to invert
        alu.not();
        //save the output in the akku
        akku.setValue(z.getValue());
    }

    private void halt(){
        shouldHalt = true;
        Debug.send("SHOULDHALT");
    }

    public Speicher getSpeicher(){
        return speicher;
    }

    public Register getAkku(){
        return akku;
    }

    public Register getIAR() {
        return iar;
    }

    public Register getIR() {
        return ir;
    }

    public int getNextAdress(){
        return iar.getValue();
    }
    public int getLastAdress(){
        return lastExecutedAdress;
    }
    public int getLastChange(){
        return lastChange;
    }
}
