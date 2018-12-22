package de.c1bergh0st.gui;

public class InputController {
    private ViewController viewContr;
    
    public InputController(ViewController viewContr) {
        this.viewContr = viewContr;
    }
    
    
    public void step() {
        lock();
        //disable: run
    }
    
    public void run() {
        lock();
        //disable step
        //run
    }
    
    public void lock() {
        //lock code with offset
        //disable lock
        //enable restore
    }
    
    public void restore() {
        //disable restore enable lock
        //enable run, step
    }
    
}
