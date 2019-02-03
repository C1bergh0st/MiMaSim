package de.c1bergh0st.mima.parsing;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.mima.instructions.InstructionMaster;
import de.c1bergh0st.visual.DialogUtil;
import de.c1bergh0st.visual.ParseUtil;

import java.awt.datatransfer.MimeTypeParseException;
import java.util.HashMap;
import java.util.LinkedList;

public class MiMaBuilder {
    private static final String MARKER = "([a-z]*:)";
    private static final String COMMANDS_WITH_ARGS = "(LDC|LDV|STV|ADD|AND|OR|XOR|EQL|JMP|JMN|LDIV|STIV)";
    private static final String COMMANDS_NO_ARGS = "(RAR|NOT|HALT|SKIP)";
    private static final String VARIABLEDECLARATION = "^var [a-zA-Z]+ = [0-9]+(, ?[0-9]+)?$";
    private Steuerwerk mima;
    private InstructionMaster instructionMaster;
    private LinkedList<String> warnings;


    public MiMaBuilder(InstructionMaster instructionMaster) {
        this.instructionMaster = instructionMaster;
    }

    public Steuerwerk createFromCode(String code, int offset) throws MiMaSyntaxException, MiMaParsingException {
        warnings = new LinkedList<>();
        long startTime = System.nanoTime();
        //Splitting the Input into Lines
        String[] lines = code.split("\\r?\\n");

        //if the offset is to big it should be ignored
        if (offset + lines.length >= Steuerwerk.MAX_ADRESS) {
            DialogUtil.showErrorToUser("Offset Error", "Offset was too big so it was set to 0");
            offset = 0;
        }

        //checks each line to match the format
        validateFullSyntax(lines);

        //Parses the Code into Direct Instructions
        String[] parsedLines = attemptParse(lines, offset);

        String l;
        for (int i = 0; i < parsedLines.length; i++){
            l = parsedLines[i];
            if(l == null){
                l = "";
                parsedLines[i] = "";
            }
            if(!(l.matches("^[A-Z]+( [0-9]+)?$") || l.matches("^[0-9]*$"))){
                throw new MiMaParsingException("Simplified Code on Line " + i + " was Wrong: \"" + l + "\"");
            }
        }

        //Parses the Direct Instructions into ByteCode:
        int[] byteCode = generateByteCode(parsedLines);

        int[] mem = new int[Steuerwerk.MAX_ADRESS+1];
        for(int i = 0; i < byteCode.length; i++){
            mem[offset+i] = byteCode[i];
        }

        mima = new Steuerwerk(instructionMaster);
        mima.getSpeicher().setMem(mem);
        long elapsedNanoTime = System.nanoTime()-startTime;
        double elapsedSeconds = (elapsedNanoTime / 1_000_000_000.0);
        Debug.send("Compilation successful in " + elapsedSeconds +"s");
        //TODO: Print warnings
        return mima;
    }

    private int[] generateByteCode(String[] simplified) throws MiMaParsingException {
        int[] byteCode = new int[simplified.length];
        for (int i = 0; i < simplified.length; i++) {
            byteCode[i] = parseLine(simplified[i], i);
        }
        return byteCode;
    }

    /**
     * Parses a Single line of simplified opCodes
     * @param line the line containing the opCode
     * @return  The matching binary code
     */
    public int parseLine(String line, int index) throws MiMaParsingException {
        line = line.trim();
        int res;
        /*
        0. trim line
        1. Check for SKIP
        2. Check for binary
        3. let InstructionMaster parse the Simplified Line

        */
        if(line.matches("SKIP")){
            return instructionMaster.getSkipCode() << 16;
        }


        if(line.matches("^[0-9]+$")) {
            int binary = ParseUtil.mask24(Integer.parseInt(line));
            if (binary != Integer.parseInt(line)) {
                addWarning("Overflow occured in Initial Variable Value");
            }
            return binary;
        }

        try{
            res = ParseUtil.mask24(instructionMaster.parseSimpleLine(line));
        } catch (IllegalArgumentException e){
            throw new MiMaParsingException("Error: \"" + line + "\" on line " + index + ", " + e.getMessage());
        }

        return res;
    }


    /**
     * Checks the Syntax of the Given String[] line by line and throws a MiMaSyntaxException if the Syntax is wrong
     * Example of a correct line: marker: ADD 2 //comment
     * Example of a malformed line: marker ADD /comment
     *
     * @param lines An Array of Instructions
     * @throws MiMaSyntaxException is thrown if a Line does not conform to the syntax
     */
    private void validateFullSyntax(String[] lines) throws MiMaSyntaxException {
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
        //1.Remove Comment from the String
        //2.1. Variable Decleration         x   done
        //2.2. Statement                    v
        //2.2.1 Marker:Command              v
        //2.2.2 Command                     v
        //Command                           v
        //1. CommandNoArg                   x
        //Comment = (//.*)
        //Marker = ([a-zA-Z]+: ?)

        String commentFree = ParsingTools.removeComment(line.replaceAll(" +", " ")).trim();
        //if the commentFreeLine still contains an /, the syntax is violated
        if(commentFree.contains("/")){
            throw new MiMaSyntaxException("Stray / Appeared on Line: " + lineNumber + ".");
        }

        //if the line is a valid variable declaration we can return if there is no overlap with Commands
        if(commentFree.matches(VARIABLEDECLARATION)){
            //extract the variable name
            String var = ParsingTools.extractVarFromDef(commentFree);
            //we need to make sure that there are no keyword collisions
            if(containsKeywordCollisions(var)){
                throw new MiMaSyntaxException("Keyword Collision on Line: " + lineNumber + ". Dont use Keywords as variables!");
            }
            return;
        }

        //line is a command

        //if a marker is present we check if it is valid
        //if not, we remove it
        if(commentFree.matches("^[a-zA-Z]+: ?[A-Z]+( [0-9]+| [a-zA-Z]+)?$")){
            //we need to make sure that there are no keyword collisions
            String marker = commentFree.substring(0,commentFree.indexOf(":"));
            if(containsKeywordCollisions(marker)){
                throw new MiMaSyntaxException("Keyword Collision on Line: " + lineNumber + ". Dont use Keywords as variables!");
            }
            //the marker is valid so we need to remove it
            commentFree = commentFree.substring(commentFree.indexOf(":") + 1);
            commentFree = commentFree.trim();
        }

        //No arguments given, needs to match a command
        if(commentFree.matches("^[A-Z]+ ?$")){
            //The Instruction does not match any know Instruction without arguments
            String instr = commentFree.trim();
            if(!instructionMaster.getCommandList(false).contains(instr)){
                throw new MiMaSyntaxException("Unknown Command on Line: " + lineNumber + ". Maybe a missing argument?");
            }
        } else { //Arguments given
            //The Instruction dies not match any know Instruction with arguments
            String instr = commentFree.trim();
            if(instr.contains(" ")){
                instr = instr.substring(0,instr.indexOf(" "));
            }
            if(!instructionMaster.getCommandList(true).contains(instr)){
                if(line.matches("^(//.*)?$")){
                    return;
                }
                throw new MiMaSyntaxException("Unknown Command on Line: " + lineNumber + ". Maybe an argument too much?");
            }
        }
        //if this stage is reached, the given line of code has the correct syntax
    }

    /**
     * Checks for any keyword collisions of the given variable
     * @param variable The Variable to check for collsions
     * @return whether the variable is a collision
     */
    @SuppressWarnings("RedundantIfStatement")
    private boolean containsKeywordCollisions(String variable){
        //Iterate through a List of all keywords
        for(String command: instructionMaster.getFullCommandList()){
            if(variable.trim().matches("^"+command+"$")){
                return true;
            }
        }
        //dont forget the var keyword
        if(variable.matches("var")){
            return true;
        }
        return false;
    }

    public String[] attemptParse(String[] linesIn, int offset) throws MiMaParsingException {
        /*
        1   Remove Comments
        2   Reduce multiple spaces to one Whitespace
        3.1 map marker definitions to their respective line + offset
        3.2 remove marker definitions
        3.3 check variable definitions for collisions -> throw MiMaParsingException
        4.1 map variables to their adresses
        4.2 OPTIONAL save their initial value
        4.3 replace var definitions with SKIP
        4.4 replace var/marker occurrences with their adresses
        5.  save the initial values at the adress line
        */
        //a map mapping a marker to its line number
        HashMap<String, Integer> markerMap = new HashMap<>();
        LinkedList<Integer[]> initialValues = new LinkedList<>();
        String[] lines = new String[linesIn.length];
        String line;
        String marker;
        int tempInt;

        System.arraycopy(linesIn, 0, lines, 0, linesIn.length);


        //1. Deleting Comments and 2. reducing multiple spaces to a single space
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("//.*$", "");
            lines[i] = lines[i].replaceAll(" +", " ");
            lines[i] = lines[i].trim();
        }

        //3.1 Map Marker Definitions to adresses + offset
        for (int i = 0; i < lines.length; i++){
            line = lines[i];
            if (line.matches("^[a-zA-Z]+: ?[A-Z]+( [0-9]+| [a-zA-Z]+)?$")) {
                //extract marker name
                int colonIndex = line.indexOf(":");
                marker = line.substring(0, colonIndex);
                //if the marker is already in the Map an MiMaParsingException is thrown
                if(markerMap.containsKey(marker)){
                    throw new MiMaParsingException("Marker Declaration Name Collision at Line "
                                                + markerMap.get(marker) + " and Line " + i);
                }
                markerMap.put(marker, i + offset);
            }
        }

        //3.2 remove Marker Definitions
        for (int i = 0; i < lines.length; i++){
            line = lines[i];
            if (line.matches("^[a-zA-Z]+: ?[A-Z]+( [0-9]+| [a-zA-Z]+)?$")) {
                int colonIndex = line.indexOf(":");
                //if there is a space after the colon remove that too
                if(line.charAt(colonIndex + 1) == ' '){
                    lines[i] = line.substring(colonIndex + 1);
                } else {
                    lines[i] = line.substring(colonIndex);
                }
            }
        }

        //3.3 check varialbe Definitions for marker collsisons
        for (int i = 0; i < lines.length; i++){
            line = lines[i];
            if (line.matches(VARIABLEDECLARATION)) {
                String temp = line.substring(4);
                int index = line.indexOf(" ");
                String variableName = temp.substring(0,index);
                if(markerMap.containsKey(variableName)){
                    throw new MiMaParsingException("Variable Naming collision. Variable Name at Line " + i
                                                + " intersects with Marker Name at Line " + markerMap.get(variableName));
                }
            }
        }


        //4.1 map Variables to their adresses and 4.2 OPTIONAL save their inital Value
        //and 4.3 replace Declaration with SKIP
        for (int i = 0; i < lines.length; i++){
            line = lines[i];
            if (line.matches(VARIABLEDECLARATION)){
                String temp = line.substring(4);
                int index = temp.indexOf(" ");
                String variableName = temp.substring(0,index);
                index = line.indexOf("=");
                int adress;
                //Advanced Variable declaration
                if(line.contains(",")){
                    String args = line.substring(index + 1);
                    index = args.indexOf(",");
                    adress = Integer.parseInt(args.substring(0,index).trim()) + offset;
                    //save initial Value
                    int value = Integer.parseInt(args.substring(index + 1).trim());
                    Integer[] pos = {adress , value};
                    initialValues.add(pos);
                } else {
                    adress = Integer.parseInt(line.substring(index + 1).trim()) + offset;
                }
                markerMap.put(variableName,adress);
                //replace declaration with SKIP
                lines[i] = "SKIP";
            }
        }

        //4.4 replace marker/variable occurrances with their line number
        for (int i = 0; i < lines.length; i++){
            line = lines[i].trim();
            if (line.matches("^[A-Z]+ [a-zA-Z]+$")){
                int index = line.indexOf(" ");
                marker = line.substring(index + 1);
                //make sure this is not a variable
                if(markerMap.containsKey(marker)){
                    int value = markerMap.get(marker);
                    lines[i] = line.substring(0,index + 1) + value;
                } else{
                    throw new MiMaParsingException("Variable/Marker at Line " + i + " not recognized");
                }
            }
        }

        for (int i = 0; i < initialValues.size(); i++){
            int adress = initialValues.get(i)[0];
            int value = initialValues.get(i)[1];
            //make sure the array is big enought
            if(adress > lines.length - (offset + 1)){
                if(adress < Steuerwerk.MAX_ADRESS + 1){
                    String[] newLines = new String[adress+1];
                    System.arraycopy(lines, 0, newLines, 0, lines.length);
                    for(int x = 0; x < newLines.length; x++){
                        if(x < lines.length){
                            newLines[x] = lines[x];
                        } else {
                            newLines[x] = "0";
                        }
                    }
                    lines = newLines;
                } else {
                    throw new MiMaParsingException("Variable Adress too big >" + (Steuerwerk.MAX_ADRESS - offset));
                }
            }

            line = lines[adress];
            if(!(line.equals("0") || line.equals("SKIP"))){
                throw new MiMaParsingException("Variable value at adress " + (adress + offset) + " would overwrite Code");
            }
            lines[adress] = Integer.toString(value);
        }


        //trim that son of a gun & remove empty
        for(int i = 0; i < lines.length; i++){
            lines[i] = lines[i].trim();
            if(lines[i].equals("")){
                lines[i] = "LDC 0";
            }
        }


        //final check
        for (int i = 0; i < lines.length; i++){
            line = lines[i];
            if(!line.matches("^[A-Z]+( ?[0-9]+)?$") && !line.matches("[0-9]+")){
                throw new MiMaParsingException("Line " + i + ":\""+ line +"\" Error after High-level Parsing");
            }

        }


        return lines;
    }

    private void addWarning(String s){
        if(!(warnings == null)){
            warnings.add(s);
        }
    }
}