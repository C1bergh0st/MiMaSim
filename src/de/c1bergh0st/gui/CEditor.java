package de.c1bergh0st.gui;

import javax.swing.JScrollPane;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;

import javax.swing.SwingUtilities;

import javax.swing.event.DocumentEvent;

import javax.swing.event.DocumentListener;

import javax.swing.text.BadLocationException;



public class CEditor extends JScrollPane{
    private static final long serialVersionUID = 5449228765795411824L;

    public JTextArea textArea;
    private LineNumberModelImpl lineNumberModel;
    private LineNumberComponent lineNumberComponent;
    
    public CEditor(JTextArea textArea) {
        super(textArea);
        this.textArea = textArea;
        lineNumberModel = new LineNumberModelImpl();
        lineNumberComponent = new LineNumberComponent(lineNumberModel);
        this.setRowHeaderView(lineNumberComponent);
        lineNumberComponent.setAlignment(LineNumberComponent.RIGHT_ALIGNMENT);
        
        this.textArea.getDocument().addDocumentListener(new DocumentListener(){
            
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
        
        this.textArea.setText("ADD 2\nmarker:NOT\nHALT");
        
    }

    private class LineNumberModelImpl implements LineNumberModel{

        @Override
        public int getNumberLines() {
            return textArea.getLineCount();
        }
        
        @Override
        public Rectangle getLineRect(int line) {
            try{
                return textArea.modelToView(textArea.getLineStartOffset(line));
            }catch(BadLocationException e){
                e.printStackTrace();
                return new Rectangle();
            }
        }
    }
}

