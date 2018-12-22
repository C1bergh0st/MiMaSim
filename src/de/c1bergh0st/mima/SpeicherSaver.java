//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies

package de.c1bergh0st.mima;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.visual.ParseUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class SpeicherSaver {

    /*
      Memory Format:
      Adress            Value
      00000-FFFFF       000000-FFFFFF newline
      00000-FFFFF       000000-FFFFFF newLine

     */

    public static void saveMemory(int mem[],JFrame parent,String[] comments){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MiMa Memory (.bin)", "bin");
        fileChooser.setFileFilter(filter);
        Debug.sendErr("called");
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(!file.getName().matches(".*\\.bin$")){
                Debug.send(file.getName());
                file = new File(file.getPath()+".bin");
            }
            // save to file
            PrintWriter writer = null;
            Debug.send(file.getPath());
            if(!file.exists()){
                Debug.send("creating");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                writer = new PrintWriter(file);
                String adress = "";
                String value = "";
                String comment = "";
                for(int i = 0; i < mem.length; i++){
                    adress = String.format("%05X", i);
                    adress = adress.substring(adress.length()-5);
                    value = String.format("%06X", mem[i]);
                    if(i < comments.length){
                        comment = comments[i];
                        if(!comment.equals("")){
                            comment = "{"+comment+"}";
                        }
                    } else {
                        comment = "";
                    }
                    if(value.equals("000000") && comment.equals("")){
                        continue;
                    }
                    if(value.length() > 6){
                        value = value.substring(adress.length()-6);
                    }
                    writer.println(adress+"-"+value+comment);
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadMemory(JFrame parent, Speicher speicher){
        int[] res = new int[Steuerwerk.MAX_ADRESS+1];
        JFileChooser fileChooser = new JFileChooser();
        Debug.sendErr("Loading File");
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                int adress = 0;
                int value = 0;
                String comment = "";
                int i = 0;
                while (line != null && i <= Steuerwerk.MAX_ADRESS) {
                    //If the line given matches the Regex we parse it into the res[]
                    if(line.matches("^([0-9A-F]{5})-([0-9A-F]{6})(\\{.*\\})?$")){
                        //Parsing
                        adress = parseSaveLineAdress(line);
                        value = parseSaveLineValue(line);
                        comment = parseSaveLineComment(line);
                        speicher.setMem(adress, value);
                        Debug.send(adress+":"+value+" //"+ comment);
                    }
                    line = reader.readLine();
                    i++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static int parseSaveLineValue(String line){
        //       Index   0123456789AB
        //Format Example 00001-FCA10D
        //line will match the Value Formatof 000000 to FFFFFF from index 6 to
        int res = Integer.parseInt(line.substring(6,12),16);
        return ParseUtil.mask24(res);
    }

    private static int parseSaveLineAdress(String line){
        //line will match the Adress Format 00000 to FFFFF in the First 5 characters of the String
        int res = Integer.parseInt(line.substring(0,5),16);
        return ParseUtil.mask20(res);
    }

    private static String parseSaveLineComment(String line){
        String res = "";
        if(line.matches("^([0-9A-F]{5})-([0-9A-F]{6})(\\{.*\\})$")){
            res = line.substring(line.indexOf('{'));
            res = res.substring(1,res.length()-1);
            return res;
        }
        return "";
    }

}
