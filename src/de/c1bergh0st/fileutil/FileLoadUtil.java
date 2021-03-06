package de.c1bergh0st.fileutil;

import de.c1bergh0st.debug.Debug;
import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.visual.DialogUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class FileLoadUtil {


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void showSaveDialog(JFrame parent, String programm){
        JFileChooser fileChooser = new JFileChooser();

        //Set the current directory
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);

        //Set the filename filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MiMa Memory (.bin)", "bin");
        fileChooser.setFileFilter(filter);

        //executes if the user pressed save
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            //if the file doesn't match the .bin we force the .bin
            if(!file.getName().matches(".*\\.bin$")){
                //Debug.send(file.getName());
                file = new File(file.getPath()+".bin");
            }

            // save to file
            PrintWriter writer;
            //Debug.send(file.getPath());

            //if the file doesn't exist we create it
            if(!file.exists()){
                Debug.send("creating");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    //If the file creation fails we should just return
                    e.printStackTrace();
                    DialogUtil.showErrorToUser("ERROR","Error while saving file:\n" + e.getMessage());
                    return;
                }
            }

            try {
                writer = new PrintWriter(file);
                writer.print(programm);
                writer.close();
            } catch (FileNotFoundException e) {
                //Should not happen, if the file didn't exist the method returns earlier
                e.printStackTrace();
            }
        }
    }

    public static String showLoadDialog(JFrame parent){
        JFileChooser fileChooser = new JFileChooser();

        //Set the current directory
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);

        //Set the filename filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MiMa Memory (.bin)", "bin");
        fileChooser.setFileFilter(filter);


        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            Debug.sendErr("Loading File");
            StringBuilder sb = new StringBuilder();
            File file = fileChooser.getSelectedFile();
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while ( line != null){
                    sb.append(line);
                    sb.append("\n");
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                DialogUtil.showErrorToUser("Error while Loading from file", e.getMessage());
            }
            return sb.toString();
        }
        return "#";
    }




}
