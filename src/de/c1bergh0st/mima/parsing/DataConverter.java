package de.c1bergh0st.mima.parsing;

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
     * @param value The Integer which holds the 24 bit value
     * @return A String Representation
     */
    public static String getSignRepr(int value){
        return "";
    }

    /**
     * Returns a String with a Command Representation of the given 24 bit value
     * @param value The Integer which holds the 24 bit value
     * @return A Code Representation
     */
    public static String getCodeRepr(int value){
        return "";
    }
    /**
     * Returns a String with a Binary Representation of the given value
     * @param value The Integer which holds the value
     * @param length The length the Binary representation should have
     * @return A Code Representation
     */
    public static String getBinary(int value, int length){
        String result = "";
        int temp = value;
        for(int i = 0; i < length; i++){
            result = (temp % 2) + result;
            temp = temp / 2;
        }
        return result;
    }
}
