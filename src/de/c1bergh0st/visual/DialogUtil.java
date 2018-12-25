//Copyright (C) 2018  Philipp Berdesinski
package de.c1bergh0st.visual;

import javax.swing.*;

public class DialogUtil {

    public static void showErrorToUser(String title,String message){
        JOptionPane.showMessageDialog(null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showDialogToUser(String title,String message){
        JOptionPane.showMessageDialog(null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

}
