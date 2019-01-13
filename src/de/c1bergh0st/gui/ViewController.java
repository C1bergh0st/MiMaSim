package de.c1bergh0st.gui;

import de.c1bergh0st.config.Colors;
import de.c1bergh0st.visual.DialogUtil;

import javax.swing.*;

public class ViewController {
    private static final int REDNESS = 60;
    private MainView mainView;
    private JFrame frame;

    public ViewController(){

    }

    public void setMainView(MainView mainView){
        this.mainView = mainView;
    }
    
    public void setEditorText(String s) {
        mainView.editor.setText(s);
    }


    public String getEditorText() {
        return mainView.editor.getText();
    }

    public JFrame getFrame(){
        return frame;
    }

    public void setFrame(JFrame frame){
        this.frame = frame;
    }


    public void setRightPanel(JComponent input){
        mainView.splitPane.setRightComponent(input);
        mainView.splitPane.setDividerLocation(mainView.splitPane.getWidth()-800);
    }

    public int getOffset(){
        return (Integer) mainView.offsetSpinner.getValue();
    }
    
    public void showException(Exception e){
        DialogUtil.showErrorToUser("ERROR", e.toString());
    }
    //Button Enabling/Disabling

    public void setEditorEnabled(boolean enabled){
        mainView.editor.setEditable(enabled);
        mainView.editor.setEnabled(enabled);
        if(enabled){
            mainView.editor.setBackground(Colors.EDITOR_ENABLED);
        } else {
            mainView.editor.setBackground(Colors.EDITOR_DISABLED);
        }
    }


    public void setDisposeEnabled(boolean enabled){
        mainView.disposeBtn.setEnabled(enabled);
    }

    public void setBuildEnabled(boolean enabled){
        mainView.buildBtn.setEnabled(enabled);
    }
}
