package de.c1bergh0st.gui;

public class ViewController {
    private MainView mainView;
    
    public ViewController(MainView mainView) {
        this.mainView = mainView;
    }
    
    public void setEditorText(String s) {
        //mainView.editor.setText(s);
    }
    
    public String getEditorText() {
       // return mainView.editor.getText();
        return "";
    }
    
    
    //Button Enabling/Disabling
    
    public void setRestoreEnabled(boolean enabled) {
        mainView.restoreBtn.setEnabled(enabled);
    }
    
    public void setLockEnabled(boolean enabled) {
        mainView.lockBtn.setEnabled(enabled);
    }
    
    public void setStepEnabled(boolean enabled) {
        mainView.stepBtn.setEnabled(enabled);
    }
    
    public void setRunEnabled(boolean enabled) {
        mainView.runBtn.setEnabled(enabled);
    }
    
}
