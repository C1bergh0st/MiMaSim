//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima.executing.visual;

import de.c1bergh0st.mima.Speicher;
import de.c1bergh0st.mima.parsing.DataConverter;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

class TableListener implements TableModelListener {
    private JTable table;
    private Speicher speicher;
    private long lastchange;

    public TableListener(JTable table, Speicher speicher) {
        this.table = table;
        this.speicher = speicher;
        lastchange = System.currentTimeMillis();
        revalidate();

    }


    public void revalidate(){
        for(int i = 0; i < table.getRowCount(); i++){
            updateRow(i);
        }
    }

    //Remains of older versions only kept around as to not forget how this works
    public void tableChanged(TableModelEvent e) {
        /*int adress = e.getFirstRow();
        int inputField = e.getColumn();
        String value = (String)table.getModel().getValueAt(adress, inputField);

        //Stop
        if(lastchange > System.currentTimeMillis() - 100){
            return;
        }
        lastchange = System.currentTimeMillis();
        }*/
    }

    /**
     * Forces all Rows to update
     * Mostly Used to ensure Colors match
     * @param adress the line/adress to update
     */
    private void updateRow(int adress){
        forceUpdate(adress,0);
        updateBinary(adress);
        updateRepr(adress);
        updateDec(adress);
        forceUpdate(adress,4);
    }

    private void forceUpdate(int adress, int column){
        table.getModel().setValueAt(table.getModel().getValueAt(adress,column),adress,column);
    }

    private void updateBinary(int adress) {
        table.getModel().setValueAt(DataConverter.getBinary(speicher.getMem(adress),24),adress,1);
    }

    private void updateDec(int adress){
        table.getModel().setValueAt(""+DataConverter.getSignRepr(speicher.getMem(adress)),adress,3);
    }

    private void updateRepr(int adress) {
        int value = speicher.getMem(adress);
        table.getModel().setValueAt(DataConverter.getCodeRepr(value),adress,2);
    }

}
