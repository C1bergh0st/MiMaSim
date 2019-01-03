package de.c1bergh0st.mima;

import java.util.HashMap;
import java.util.LinkedList;

public class RegisterMaster {

    private HashMap<String, Register> nameMap;
    private LinkedList<String> nameList;

    public RegisterMaster(){
        nameMap = new HashMap<>();
        nameList = new LinkedList<>();
    }

    /**
     * Adds a new Register to this List and associates it with the given String name
     * @param name The name of the Register
     * @param register  The Register
     */
    public void addRegister(String name, Register register){
        if(nameMap.containsKey(name)){
            throw new IllegalArgumentException("Name \"" + name +"\" already in use!");
        } else if(register == null){
            throw new NullPointerException("Register can't be null!");
        } else if(name == null){
            throw new NullPointerException("Name cant be null!");
        }
        //map the name to the highest index (length -1) of the list
        nameMap.put(name,register);
        //add the name of the register to the list
        nameList.add(name);
    }

    /**
     * Returns the Register associated with the given Name
     * @param name The name of the Register
     * @return  The Register with the name name
     */
    public Register get(String name){
        if(name == null){
            throw new NullPointerException("Name cant be null!");
        } else if(!nameMap.containsKey(name)){
            throw new IllegalArgumentException("Name \"" + name +"\" is not in Use!");
        }
        return nameMap.get(name);
    }


    /**
     * Removes a register from the List via its name
     * @param name The name of the Register which should be removed
     */
    public void removeRegister(String name){
        if(name == null){
            throw new NullPointerException("Name cant be null!");
        } else if(!nameMap.containsKey(name)){
            throw new IllegalArgumentException("Name \"" + name +"\" is not in Use!");
        }
        nameMap.remove(name);
        if(!nameList.remove(name)){ //if remove(name) returns false the list ist synchronous with the nameMap!
            throw new IllegalStateException("Name \"" + name +"\" was not contained in the nameList!");
        }
    }

    /**
     * Returns a LinkedList containing all Keys currently in use
     * @return The List of Keys
     */
    public LinkedList<String> getList(){
        LinkedList<String> copy = new LinkedList<>();
        for(String s : nameList){
            copy.add(s);
        }
        return copy;
    }

    /**
     * Sets the value of the register with the name name
     * @param name  The name of the Register
     * @param value The new Value of the Register
     */
    public void setValue(String name, int value){
        get(name).setValue(value);
    }

    /**
     * Gets the value of the register with the name name
     * @param name  The name of the Register
     */
    public int getValue(String name){
        return get(name).getValue();
    }



}
