//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
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
     *
     * @param binaryString A String to Test
     * @return Wheter the String only contains 1's and 0's and is max 24 'bits' long
     */
    public static boolean validBinary(String binaryString){
        String trimmedString = binaryString.replaceAll("\\s","");
        while(trimmedString.length() < 24){
            trimmedString = "0" + trimmedString;
        }

        return trimmedString.matches("^([1,0]{24})$");
    }


    /**
     * @param commandString Command
     * @return Whether the given commandString is valid
     */
    public static boolean validCommand(String commandString){
        String s = commandString.replaceAll("\\s","");
        if(s.matches("^(((LDC|LDV|STV|ADD|AND|OR|XOR|EQL|JMP|JMN|LDIV|STIV)[0-9]+)|(RAR|NOT|EMPTY|HALT))$")){
            return true;
        }
        Debug.send("Regex didnt match: "+s);
        return false;
    }


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
    @SuppressWarnings("ConstantConditions")
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

    /**
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

    public static int getDisplayValue(String binary){
        if(binary.length() == 24){
            if(binary.charAt(0) == '0'){
                return Integer.parseInt(binary.substring(1));
            }
            return ((-1) * (Integer.parseInt(binary.substring(1)))+1);
        }
        return -1;
    }

    public static int getDisplayValue(int i, boolean adress){
        if(!adress){
            if(i >= 0 && i <= Steuerwerk.MAX_VALUE){
                if(i < 0b100000000000000000000000){
                    return i;
                }
                return (-1) * (0b011111111111111111111111-(i % 0b100000000000000000000000) + 1);
            }
        } else{//20 Bit Adresses
            if(i >= 0 && i <= Steuerwerk.MAX_ADRESS){
                return i;
            }
        }
        return -1;
    }

    /**
     * @param i Integer
     * @return Returns an int with it's highest byte set to zero
     */
    public static int mask24(int i){
        return i & 0b00000000111111111111111111111111;
    }


    /**
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
