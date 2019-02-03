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

    private static final String SAMPLEPROGRAMM = "JMP 18\n" +
            "//Variable Declaration is done in two Ways:\n" +
            "var VARIABLE = 1\n" +
            "var VARIABLETWO = 2, 3\n" +
            "//The second Declaration also initializes adress 2 to a value of 3\n" +
            "//Comments are obviusly this\n" +
            "//IF you need help understanding the instruction-set of the MiMa\n" +
            "//use Help -> MiMa Documentation\n" +
            "//The Following Programm generates the first 20 fibonacchi numbers\n" +
            "//And saves them to adresses 40 to 59\n" +
            "\n" +
            "var ONE = 11, 1\n" +
            "var PREV = 12, 1\n" +
            "var PREVPREV = 13, 0\n" +
            "var CURR = 14, 0\n" +
            "var COUNT = 15, 16777196\n" +
            "var ADDR = 16, 40\n" +
            "\n" +
            "loop: LDC 0 //STARTLOOP\n" +
            "ADD PREV     //curr = prev + prevprev\n" +
            "ADD PREVPREV\n" +
            "STV CURR\n" +
            "STIV ADDR //mem[<addr>] = curr;\n" +
            "LDV ADDR\t//ADDR++;\n" +
            "ADD ONE\n" +
            "STV ADDR\n" +
            "LDV PREV // prevprev = prev\n" +
            "STV PREVPREV\n" +
            "LDV CURR\t// prev = curr\n" +
            "STV PREV\n" +
            "LDV COUNT //ENDLOOP\n" +
            "ADD ONE\n" +
            "STV COUNT\n" +
            "JMN loop //Ends if Count is positive\n" +
            "HALT";

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
        
        this.textPane.setText(SAMPLEPROGRAMM);
        
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
            } catch (NullPointerException e){
                //Error appeares when line wrapping occures
                textPane.repaint();
                return new Rectangle();
            }
        }
    }
}

