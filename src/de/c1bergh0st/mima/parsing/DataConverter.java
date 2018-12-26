package de.c1bergh0st.mima.parsing;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.visual.ParseUtil;

/**
 * Implements converting to the 4 Data Types Used in the MiMa Runtime Visualization
 * Data Types:
 * Main Type:   The 24 bit value stored in a single register (not signed)
 * 1. String:   The Binary Representation of the Main Type
 * 2. String:   A signed representation of the 24 bit value stored in Main Type
 * 3. String:   A Normal Command with or without an Argument
 */
public class DataConverter {

    /**
     * Returns a String with the value of a 24 bit signed integer stored in the input integer
     * Example:
     * 0b111111111111111111111111 -> -1
     * 0b000000000000000000000001 -> 1
     * 0b000000000000000000000111 -> 7
     *
     * @param value The Integer which holds the 24 bit value
     * @return A String Representation
     */
    public static String getSignRepr(int value) {
        value = ParseUtil.mask24(value);
        int wert;
        if (value / 0b100000000000000000000000 == 0) {
            wert = value % 0b100000000000000000000000;
        } else {
            wert = -0b100000000000000000000000 + (value % 0b100000000000000000000000);
        }
        return Integer.toString(wert);
    }

    /**
     * Returns a String with a Command Representation of the given 24 bit value
     *
     * @param value The Integer which holds the 24 bit value
     * @return A Code Representation
     */
    public static String getCodeRepr(int value) {
        String result;
        byte firstByte = (byte) (ParseUtil.mask24(value) >>> 20);
        if (firstByte >= 0 && firstByte <= 11) {
            result = Steuerwerk.commandSet[firstByte];
            result += " ";
            result += getSignRepr(ParseUtil.mask20(value));
        } else if (firstByte >= 12 && firstByte <= 14) {
            result = "SKIP";
        } else if (firstByte == 15) {
            byte secondByte = (byte) (ParseUtil.mask20(value) >>> 16);
            switch (secondByte) {
                case 0:
                    result = "HALT";
                    break;
                case 1:
                    result = "NOT";
                    break;
                case 2:
                    result = "RAR";
                    break;
                default:
                    result = "SKIP";
                    break;
            }
        } else {
            Debug.sendErr("INSTRUCTION > 0B1111", 2);
            result = "ERROR";
        }

        return result;
    }



    /**
     * Returns a String with a Binary Representation of the given value
     *
     * @param value  The Integer which holds the value
     * @param length The length the Binary representation should have
     * @return A Code Representation
     */
    public static String getBinary(int value, int length) {
        String result = "";
        int temp = value;
        for (int i = 0; i < length; i++) {
            result = (temp % 2) + result;
            temp = temp / 2;
        }
        return result;
    }
}
