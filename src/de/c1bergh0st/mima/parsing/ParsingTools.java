package de.c1bergh0st.mima.parsing;

import de.c1bergh0st.mima.instructions.InstructionMaster;

import java.util.Scanner;

public class ParsingTools {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args){
        System.out.println("aa//a".indexOf("//"));
        System.out.println(removeComment("ab//cdefg//hijkl//mnop"));
        System.out.println(extractVarFromDef("var ADD = 2,3"));
        InstructionMaster master = new InstructionMaster();
        master.loadMiMa();
        MiMaBuilder mb = new MiMaBuilder(master);
        Scanner scan = new Scanner(System.in);
        try{
            mb.validateSyntax("var ADD = 20",10);
        } catch (MiMaSyntaxException e){
            e.printStackTrace();
        }
        while(true){
            try{
                mb.validateSyntax(scan.nextLine(),0);
                System.out.println("success");
            } catch (MiMaSyntaxException e){
                e.printStackTrace();
            }
        }
    }

    public static String extractVarFromDef(String line){
        //given Line: "var MARKER: 2(,2)?"
        String shorterLine = line.substring(4);
        int end = shorterLine.indexOf("=");
        return shorterLine.substring(0,end-1);
    }

    /**
     * Removes anything after the first occurrence of "//"
     * @param line The Line of code which my or may not contain the Comment
     * @return The same Line of code without any comments
     */
    public static String removeComment(String line){
        //no comment present on this Line
        if (!line.matches("^[^/]*//.*$")){
            return line;
        } else{
            int end = line.indexOf("//");
            return line.substring(0,end);
        }
    }

}
