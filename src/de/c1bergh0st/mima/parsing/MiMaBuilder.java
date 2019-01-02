package de.c1bergh0st.mima.parsing;

import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.mima.instructions.InstructionMaster;
import de.c1bergh0st.visual.DialogUtil;
import de.c1bergh0st.visual.ParseUtil;

import java.util.HashMap;

public class MiMaBuilder {
    private static final String MARKER = "([a-z]*:)";
    private static final String COMMANDS_WITH_ARGS = "(LDC|LDV|STV|ADD|AND|OR|XOR|EQL|JMP|JMN|LDIV|STIV)";
    private static final String COMMANDS_NO_ARGS = "(RAR|NOT|HALT|SKIP)";
    private static final String VALIDVARIABLENAME = "([A-Z]{3,})";
    private static final String VARIABLEDECLARATION = "^var("+ VALIDVARIABLENAME +")=[0-9]+(\\/\\/.*)?$";
    private Steuerwerk mima;
    private InstructionMaster instructionMaster;


    public MiMaBuilder(InstructionMaster instructionMaster) {
        this.instructionMaster = instructionMaster;
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
        validateSyntaxFull(lines);
        //Parses the Code into Direct Instructions
        String[] parsedLines = attemptParse(lines, offset);
        //Parses the Direct Instructions into ByteCode:
        int[] byteCode = generateByteCode(parsedLines);

        int[] mem = new int[Steuerwerk.MAX_ADRESS+1];
        for(int i = 0; i < byteCode.length; i++){
            mem[offset+i] = byteCode[i];
        }

        mima = new Steuerwerk(instructionMaster);
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
     * @throws MiMaSyntaxException is thrown if a Line does not conform to the syntax
     */
    private void validateSyntaxFull(String[] lines) throws MiMaSyntaxException {
        for (int i = 0; i < lines.length; i++) {
            validateSyntax(lines[i], i);
        }
    }

    /**
     * Validates a single Line of MiMa high level code
     * @param line The Line which should be checked
     * @throws MiMaSyntaxException is thrown if the line is invalid
     */
    public void validateSyntax(String line, int lineNumber) throws MiMaSyntaxException {
        String VARIABLEDECLARATION = "^var [a-zA-Z]+ = [0-9]+(, ?[0-9]+)?$";
        //1.Remove Comment from the String
        //2.1. Variable Decleration         x
        //2.2. Statement                    v
        //2.2.1 Marker:Command              v
        //2.2.2 Command                     v
        //Command                           v
        //1. CommandNoArg                   x
        //2. CommandArg                     v
        //2.1 CommandArg var                x
        //2.1 CommandArg marker             x
        //2.1 CommandArg number             x
        //Comment = (//.*)
        //Marker = ([a-zA-Z]+: ?)
        String normalized = line.replaceAll("  ", " ");

        String commentFree = ParsingTools.removeComment(normalized);
        //if the commentFreeLine still contains an /, the syntax is violated
        if(commentFree.contains("/")){
            throw new MiMaSyntaxException("Stray / Appeared on Line: " + lineNumber + ".");
        }

        //if the line is a valid variable declaration we can return if there is no overlap with Commands
        if(commentFree.matches(VARIABLEDECLARATION)){
            String marker = ParsingTools.extractVarFromDef(commentFree);
            for(String command: instructionMaster.getCommandList()){
                if(marker.matches("^"+command+"$")){
                    throw new MiMaSyntaxException("Keyword Collision on Line: " + lineNumber + ". Dont use Keywords as variables!");
                }
            }
            return;
        }


    }

    public String[] attemptParse(String[] linesIn, int offset) throws MiMaParsingException {
        //a map mapping a marker to its line number
        HashMap<String, Integer> markermap = new HashMap<String, Integer>();
        String[] lines = new String[linesIn.length];
        String line;
        int tempInt;
        String marker;

        System.arraycopy(linesIn, 0, lines, 0, linesIn.length);


        //Deleting Comments and whitespaces
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("//.*$", "");
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
                    markermap.put(marker, i + offset);
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