//Copyright (C) 2018  Philipp Berdesinski
package de.c1bergh0st.visual;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.Steuerwerk;

/**
 *  Just a Utility Class to convert from Strings to binary and vice versa
 */
@SuppressWarnings({"StringConcatenationInLoop", "WeakerAccess"})
public class ParseUtil {

    //A List of possible Commands
    public static final String[] commands = {"LDC", "LDV", "STV", "ADD", "AND", "OR", "XOR", "EQL", "JMP", "JMN", "LDIV", "STIV", "RAR", "NOT", "EMPTY", "HALT"};


    /**
     * @param command A Command witch should match the regex given in validCommand(String commandString)
     * @return A 24 bit Integer Representation of a valid Command
     */
    public static int parseCommand(String command){
        String s = command.replaceAll("\\s","");
        //We Remove characters until we only have a number left.
        while(!s.matches("^[0-9]+$") && s.length() > 1){
            s = s.substring(1);
        }
        if(!s.matches("^[0-9]+$")){
            s = "0";
        }
        //we take bits 0 to 19    Nicely prevents bigger values
        int result = mask20(Integer.parseInt(s));
        //we add bits 20 to 23 to it
        result += getOpCode(command)<<20;
        return mask24(result);
    }


    /**
     * @param command A Command that should contain a possible Command
     * @return An Integer with the OpCode of the given Command String => see ParseUtil.commands
     */
    public static int getOpCode(String command){
        int i = 0;

        //if the Command is contained within the String we get the OPCode back
        while(!command.contains(commands[i]) && i < commands.length){
            i++;
        }

        //If i == 16 we didnt find Anything so we set the code to LDC (Code :0)
        if(i == 16){
            i = 0;
        }

        return i;
    }

    /**Returns a 24bit binary representation of the Given Integer
     * Any bits outside the 24bit area will be lost
     *
     * @param i Integer
     * @return A Full Binary String Representation with leading zeros.
     */
    public static String toBinaryString(int i){
        //Create a String from the 24 bit Value of i
        String s = Integer.toBinaryString(mask24(i));
        //Pads the String with Leading zeros untill it is 24 'bits' long
        while(s.length() < 24){
            s = "0" + s;
        }
        return s;
    }

    /**Converts from a signed Binary String (e.g.: 0101000001010000010100001111) to an Integer representation
     *
     * @param binary String in signed binary form with 24 bits
     * @return Integer representation of the binary String
     */
    public static int getDisplayValue(String binary){
        if(binary.length() == 24){
            if(binary.charAt(0) == '0'){
                return Integer.parseInt(binary.substring(1));
            }
            return ((-1) * (Integer.parseInt(binary.substring(1)))+1);
        }
        return -1;
    }


    /**Masks an integer to be in Range of the MiMa Value format
     * @param i Integer
     * @return Returns an int with it's highest byte set to zero
     */
    public static int mask24(int i){
        return i & 0b00000000111111111111111111111111;
    }


    /**Masks an integer to be in Range of the MiMa Adress format
     * @param i Integer
     * @return Returns an int with it's highest 12 bits set to zero
     */
    public static int mask20(int i){
        //Return
        return i & 0b00000000000011111111111111111111;
    }


    /**
     * @param value Integer
     * @return Command reprentation of the Input
     */
    public static String code(int value){
        int safeValue = mask24(value);
        byte b = (byte)(safeValue>>>20);
        safeValue &= 0b00000000000011111111111111111111;
        if(b >= 0 && b < 16){
            String s =  commands[b] +" ";
            if(b < 12){
                s += safeValue;
            }
            return s;
        }
        return "Error";
    }
}
