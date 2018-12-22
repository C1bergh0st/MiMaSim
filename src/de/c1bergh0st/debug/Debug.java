//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.debug;

public class Debug {

    /**
     * @param errormessage The Error-message to be shown
     */
    public static void sendErr(String errormessage){
        sendErr(errormessage,0);
    }

    /**
     * @param errormessage The Error-message to be shown
     * @param level Severity of the Error 0-Normal 1-Severe 2-Fatal
     */
    public static void sendErr(String errormessage, int level){
        String type;
        switch (level){
            case 0:
                type = "Error :";
                break;
            case 1:
                type = "SEVERE Error :";
                break;
            default:
                type = "FATAL ERROR :";
                break;
        }
        System.out.println(type+errormessage);
    }

    /**
     * @param message Send a notification
     */
    public static void send(String message){
        System.out.println("Notice :"+message);
    }
}
