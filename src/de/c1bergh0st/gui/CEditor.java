package de.c1bergh0st.gui;

import javax.swing.*;
import java.awt.Rectangle;

import javax.swing.event.DocumentEvent;

import javax.swing.event.DocumentListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Utilities;


public class CEditor extends JScrollPane{
    private static final long serialVersionUID = 5449228765795411824L;

    public JTextPane textPane;
    private LineNumberModelImpl lineNumberModel;
    private LineNumberComponent lineNumberComponent;
    
    public CEditor(JTextPane textComponent) {
        super(textComponent);
        this.textPane = textComponent;
        lineNumberModel = new LineNumberModelImpl();
        lineNumberComponent = new LineNumberComponent(lineNumberModel);
        this.setRowHeaderView(lineNumberComponent);
        lineNumberComponent.setAlignment(LineNumberComponent.RIGHT_ALIGNMENT);
        
        this.textPane.getDocument().addDocumentListener(new DocumentListener(){
            
            @Override
            public void changedUpdate(DocumentEvent arg0) {
                lineNumberComponent.adjustWidth();
            }
            
            @Override
            public void insertUpdate(DocumentEvent arg0) {
                lineNumberComponent.adjustWidth();
            }
            
            @Override
            public void removeUpdate(DocumentEvent arg0) {
                lineNumberComponent.adjustWidth();
            }
        });
        
        this.textPane.setText("var MINUSONE = 20 //Simple Programm to check some but not all Commands\n" +
                "LDC 0\n" +
                "NOT\n" +
                "STV MINUSONE\n" +
                "LDC 10\n" +
                "loop: JMN end\n" +
                "ADD MINUSONE\n" +
                "JMP loop\n" +
                "end: HALT");
        
    }

    private class LineNumberModelImpl implements LineNumberModel{

        @Override
        public int getNumberLines() {
            int totalCharacters = textPane.getText().length();
            int lineCount = (totalCharacters == 0) ? 1 : 0;

            try {
                int offset = totalCharacters;
                while (offset > 0) {
                    try{
                        offset = Utilities.getRowStart(textPane, offset) - 1;
                    } catch (NullPointerException e){
                        System.out.println("Mysterious Error in CEditor");
                    }
                    lineCount++;
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            return lineCount;
        }
        
        @SuppressWarnings("deprecation")
        @Override
        public Rectangle getLineRect(int line) {
            Element map = textPane.getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);

            try{
                return textPane.modelToView(lineElem.getStartOffset());
            }catch(BadLocationException e){
                e.printStackTrace();
                return new Rectangle();
            }
        }
    }
}

