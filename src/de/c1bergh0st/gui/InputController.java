package de.c1bergh0st.gui;

import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.mima.parsing.MiMaBuilder;
import de.c1bergh0st.mima.parsing.MiMaParsingException;
import de.c1bergh0st.mima.parsing.MiMaSyntaxException;

import javax.swing.text.View;

public class InputController {
    private MiMaBuilder builder;
    private ViewController viewContr;
    private String rawCode;
    private Steuerwerk currentMiMa;
    
    public InputController(ViewController viewContr) {
        this.viewContr = viewContr;
        this.builder = new MiMaBuilder();
    }
    
    public void build(){
        //load mima
        try {
            currentMiMa = builder.createFromCode(viewContr.getEditorText(),viewContr.getOffset());
        } catch (MiMaSyntaxException e) {
            viewContr.showException(e);
            return;
        } catch (MiMaParsingException e) {
            viewContr.showException(e);
            return;
        }
        //enable lock, step, run, dispose
        viewContr.setLockEnabled(true);
        viewContr.setRunEnabled(true);
        viewContr.setStepEnabled(true);
        viewContr.setDisposeEnabled(true);
        //disable editor, build
        viewContr.setEditorEnabled(false);
        viewContr.setBuildEnabled(false);
    }

    public void dispose(){
        //unload mima
        currentMiMa = null;
        //enable build , editor
        viewContr.setEditorEnabled(true);
        viewContr.setBuildEnabled(true);
        //disable dispose, lock, step, run, restore
        viewContr.setLockEnabled(false);
        viewContr.setRunEnabled(false);
        viewContr.setStepEnabled(false);
        viewContr.setDisposeEnabled(false);
        viewContr.setRestoreEnabled(false);
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
