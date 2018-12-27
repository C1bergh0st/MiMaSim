//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.debug;

/**
 * This Class solely exists to "funel" all calls to the System.out.println method
 * and to organize logging in some way for future uses
 */
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
     * @param message a message to be shown on Console
     */
    public static void send(String message){
        System.out.println("Notice :"+message);
    }

    /**
     *
     * @param message a String to be shown on Console
     */
    public static void sendRaw(String message){
        System.out.println(message);
    }


}
