package de.c1bergh0st.gui;

import de.c1bergh0st.mima.Steuerwerk;
import de.c1bergh0st.mima.executing.visual.MiMaPanel;
import de.c1bergh0st.mima.instructions.InstructionMaster;
import de.c1bergh0st.mima.parsing.MiMaBuilder;
import de.c1bergh0st.mima.parsing.MiMaParsingException;
import de.c1bergh0st.mima.parsing.MiMaSyntaxException;

import javax.swing.*;
import javax.swing.text.View;

public class InputController {
    private MiMaBuilder builder;
    private ViewController viewContr;
    private String rawCode;
    private Steuerwerk currentMiMa;
    
    public InputController(ViewController viewContr) {
        this.viewContr = viewContr;
        InstructionMaster instructionMaster = new InstructionMaster();
        instructionMaster.loadMiMa();
        this.builder = new MiMaBuilder(instructionMaster);
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
        viewContr.setDisposeEnabled(true);
        //disable editor, build
        viewContr.setEditorEnabled(false);
        viewContr.setBuildEnabled(false);
        viewContr.setRightPanel(new MiMaPanel(currentMiMa));
    }

    public void dispose(){
        //unload mima
        currentMiMa = null;
        //enable build , editor
        viewContr.setEditorEnabled(true);
        viewContr.setBuildEnabled(true);
        //disable dispose, lock, step, run, restore
        viewContr.setDisposeEnabled(false);
        viewContr.setRightPanel(new JPanel());
    }
}
