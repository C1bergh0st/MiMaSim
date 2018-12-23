package de.c1bergh0st.mima.parsing;

import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.visual.DialogUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class MiMaBuilder {
    //Example Lines: ? = optional
    // Format: (marker:)?command(value)?
    String[] lines;
    Steuerwerk mima;



    public MiMaBuilder(){

    }

    public Steuerwerk createFromCode(String code, int offset) throws MiMaSyntaxException, MiMaParsingException {
        lines = code.split("\\r?\\n");
        //checks each line to match the format
        validateSyntax(lines);
        attemptBuild(lines, offset);
        return mima;
    }

    private void validateSyntax(String[] lines)throws MiMaSyntaxException{
        String cleanLine;
        for(int i = 0; i < lines.length; i++){
            cleanLine = lines[i].replaceAll(" ","");
            if(!lines[i].matches("^([a-z]*:)?(((LDC|LDV|STV|ADD|AND|OR|XOR|EQL|JMP|JMN|LDIV|STIV)[0-9]+)|(RAR|NOT|EMPTY|HALT))(\\/\\/.*)?$")){
                throw new MiMaSyntaxException("Line: i");
            }
        }
    }

    public String[] attemptBuild(String[] linesIn, int offset) throws MiMaParsingException{
        Steuerwerk mima = new Steuerwerk();
        Speicher speicher = mima.getSpeicher();
        //a map mapping a marker to its line number
        HashMap<String,Integer> markermap= new HashMap<String,Integer>();
        String[] lines = new String[linesIn.length];

        System.arraycopy(linesIn,0,lines,0,linesIn.length);

        if(offset + lines.length >= Steuerwerk.MAX_ADRESS){
            DialogUtil.showErrorToUser("Offset Error","Offset was too big so it was set to 0");
            offset = 0;
        }
        int[] memory = new int[Steuerwerk.MAX_ADRESS+1];

        //Deleting Comments
        for(int i = 0; i < lines.length; i++){
            lines[i] = lines[i].replaceAll("\\/\\/.*$","");
        }

        //mapping markers to lines, deleting markers:
        String line;
        String marker;
        for(int i = 0; i < lines.length; i++){
            line = lines[i];
            if(line.matches("^([a-z]*:)(((LDC|LDV|STV|ADD|AND|OR|XOR|EQL|JMP|JMN|LDIV|STIV)([0-9]+|[a-zA-Z]+))|(RAR|NOT|EMPTY|HALT))")){
                marker = line.substring(0,line.indexOf(":"));
                if(markermap.containsKey(marker)){
                    throw new MiMaParsingException("Multiple Markers at Line: " + i);
                } else{
                    //mapping the marker to its line
                    markermap.put(marker,i);
                    //deleting the marker from the source code
                    lines[i] = line.substring(line.indexOf(":")+1);
                }
            }
        }

        //replacing all occurrences of markers:
        for(int i = 0; i < lines.length; i++){
            line = lines[i];
            for(String key : markermap.keySet()){
                if(line.contains(key)){
                    line = line.replaceAll(key,Integer.toString(markermap.get(key)));
                }
            }

            lines[i] = line;
        }

        return lines;
    }
}
