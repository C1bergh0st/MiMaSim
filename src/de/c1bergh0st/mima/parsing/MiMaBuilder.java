package de.c1bergh0st.mima.parsing;

import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.visual.DialogUtil;
import de.c1bergh0st.visual.ParseUtil;

import java.util.HashMap;

public class MiMaBuilder {
    //Example Lines: ? = optional
    // Format: (marker:)?command(value)?
    //OR var NAME =
    Steuerwerk mima;
    private static final String MARKER = "([a-z]*:)";
    private static final String COMMANDS_WITH_ARGS = "(LDC|LDV|STV|ADD|AND|OR|XOR|EQL|JMP|JMN|LDIV|STIV)";
    private static final String COMMANDS_NO_ARGS = "(RAR|NOT|HALT|SKIP)";
    private static final String VARIABLEDECLARATION = "^var[A-Z]{5,}=[0-9]*$";


    public MiMaBuilder() {

    }

    public Steuerwerk createFromCode(String code, int offset) throws MiMaSyntaxException, MiMaParsingException {
        //Splitting the Input into Lines
        String[] lines = code.split("\\r?\\n");

        //if the offset is to big it should be ignored
        if (offset + lines.length >= Steuerwerk.MAX_ADRESS) {
            DialogUtil.showErrorToUser("Offset Error", "Offset was too big so it was set to 0");
            offset = 0;
        }

        //checks each line to match the format
        validateSyntax(lines);
        //Parses the Code into Direct Instructions
        String[] parsedLines = attemptParse(lines);
        //Parses the Direct Instructions into ByteCode:
        int[] byteCode = generateByteCode(parsedLines);

        int[] mem = new int[Steuerwerk.MAX_ADRESS+1];
        for(int i = 0; i < byteCode.length; i++){
            mem[offset+i] = byteCode[i];
        }

        mima = new Steuerwerk();
        mima.getSpeicher().setMem(mem);
        return mima;
    }

    private int[] generateByteCode(String[] simplified) {
        int[] byteCode = new int[simplified.length];
        for (int i = 0; i < simplified.length; i++) {
            byteCode[i] = parseLine(simplified[i]);
        }
        return byteCode;
    }

    /**
     * Parses a Single line of simplified opCodes
     * @param line the line containing the opCode
     * @return  The matching binary code
     */
    public int parseLine(String line) {
        int result = 0;
        if (line.contains("SKIP")) {
            result = 0xE00000; //opCode 15 : SKIP
        } else if (line.matches("(RAR|NOT|HALT)")) {
            result = parseExtCode(line);
        } else {
            result = parseArgCode(line);
        }
        return ParseUtil.mask24(result);
    }

    /**
     * Parses a single line of simplified opCodes with Arguments
     * @param line the line containing the opCode
     * @return The matching binary code
     */
    private int parseArgCode(String line){
        int result = 0;
        String[] split = splitArgCommand(line);
        for(int i = 0; i < Steuerwerk.commandSet.length; i++){
            if(split[0].contains(Steuerwerk.commandSet[i])){
                result = i << 20; //Shift the opCode 20 bits to the left
            }
        }
        return result + ParseUtil.mask20(Integer.parseInt(split[1]));
    }

    /**
     * Splits the given String into digits (index 0) and non-digits (index 1)
     * @param line the String you want to split
     * @return a split String-array of size 2
     */
    private String[] splitArgCommand(String line){
        String command = "";
        String value = "";
        for(int i = 0; i < line.length(); i++){
            if(Character.isLetter(line.charAt(i))){
                command += line.charAt(i);
            } else {
                value += line.charAt(i);
            }
        }
        String[] result = {command, value};
        return result;
    }


    /**
     * Takes a simplified String containing an extended opCode
     * and returns the matching binary or HALT if an Error occurred
     * @param line The simplified String containing an extended opCode
     * @return The matching binary code
     */
    private int parseExtCode(String line){
        int result = 0xF00000;
        for(int i = 0; i < Steuerwerk.extCommandSet.length; i++){
            if(line.contains(Steuerwerk.extCommandSet[i])){
                result += i * 0x010000;
            }
        }
        return result;
    }


    /**
     * Checks the Syntax of the Given String[] line by line and throws a MiMaSyntaxException if the Syntax is wrong
     * Example of a correct line: marker: ADD 2 //comment
     * Example of a malformed line: marker ADD /comment
     *
     * @param lines An Array of Instructions
     * @throws MiMaSyntaxException
     */
    private void validateSyntax(String[] lines) throws MiMaSyntaxException {
        String cleanLine;
        for (int i = 0; i < lines.length; i++) {
            cleanLine = lines[i].replaceAll(" ", "");
            if (!cleanLine.matches("^([a-z]*:)?((" + COMMANDS_WITH_ARGS + "[0-9]+)|" + COMMANDS_NO_ARGS + ")(\\/\\/.*)?$") && !cleanLine.matches(VARIABLEDECLARATION)) {
                throw new MiMaSyntaxException("Line: " + i);
            }
        }
    }

    public String[] attemptParse(String[] linesIn) throws MiMaParsingException {
        //a map mapping a marker to its line number
        HashMap<String, Integer> markermap = new HashMap<String, Integer>();
        String[] lines = new String[linesIn.length];
        String line;
        int tempInt;
        String marker;

        System.arraycopy(linesIn, 0, lines, 0, linesIn.length);


        //Deleting Comments and whitespaces
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("\\/\\/.*$", "");
            lines[i] = lines[i].replaceAll(" ", "");
        }

        //marker:ADD2
        //OR varNAME
        //getting rid of variables
        for (int i = 0; i < lines.length; i++) {
            line = lines[i];
            if (line.matches(VARIABLEDECLARATION)) {                      //varNAME=2
                line = line.substring(3);                               //NAME=2
                tempInt = line.indexOf("=");
                marker = line.substring(0, tempInt);                     //NAME
                tempInt = Integer.parseInt(line.substring(tempInt + 1));  //2
                if (markermap.containsKey(marker)) {
                    throw new MiMaParsingException("Multiple Variable name at Line: " + i);
                } else {
                    markermap.put(marker, tempInt);
                    line = "SKIP";                                      //avoid errors with offsets
                }
            }
            lines[i] = line;
        }


        //mapping markers to lines, deleting markers:
        for (int i = 0; i < lines.length; i++) {
            line = lines[i];
            if (line.matches("^([a-z]*:)(.*)$")) {
                marker = line.substring(0, line.indexOf(":"));
                if (markermap.containsKey(marker)) {
                    throw new MiMaParsingException("Multiple Markers at Line: " + i);
                } else {
                    //mapping the marker to its line
                    markermap.put(marker, i);
                    //deleting the marker from the source code
                    lines[i] = line.substring(line.indexOf(":") + 1);
                }
            }
        }

        //replacing all occurrences of markers:
        for (int i = 0; i < lines.length; i++) {
            line = lines[i];
            for (String key : markermap.keySet()) {
                if (line.contains(key)) {
                    line = line.replaceAll(key, Integer.toString(markermap.get(key)));
                }
            }

            lines[i] = line;
        }

        //if the code still contains markers a MiMaParsingException is thrown
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].matches("^([a-z]*:)?((" + COMMANDS_WITH_ARGS + "[0-9]+)|" + COMMANDS_NO_ARGS + ")$")) {
                throw new MiMaParsingException("Incorrrect Usage of Operator at Line" + i);
            }
        }
        return lines;
    }
}