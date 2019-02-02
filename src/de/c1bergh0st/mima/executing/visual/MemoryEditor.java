//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies
package de.c1bergh0st.mima.executing.visual;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import de.c1bergh0st.config.Colors;
import de.c1bergh0st.mima.*;

import java.awt.*;

public class MemoryEditor{
    private final JTable table;
    private final JScrollPane panel;
    private final CustomTableModel model;
    private final TableListener listener;
    private final Steuerwerk mima;
    private final String[][] data;
    private int shownLength;

    public MemoryEditor(Speicher speicher, Steuerwerk mima){
        this.mima = mima;
        //How much of the Memory should be shown
        //shownLength = 400;
        //shownLength = 1048576;
        shownLength = 10000;
        String[] cols = {"Adress", "Binary", "\"Code\"", "Decimal","Comments"};
        data = new String[shownLength][cols.length];



        for(int i = 0; i < shownLength; i++){
            data[i][0] = ""+i;
        }

        for(int x = 1; x < cols.length; x++){
            for(int i = 0; i < shownLength; i++){
                data[i][x] = "";
            }
        }
        model = new CustomTableModel(data, cols);
        table = new JTable(model);
        table.setEnabled(false);
        listener = new TableListener(table,speicher);
        model.addTableModelListener(listener);

        //Formatting
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(0);

        //Colorcoding rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (row == mima.getNextAdress()) {
                    setBackground(Colors.MIMA_TABLE_NEXT_EXECUTION);
                    setForeground(Color.BLACK);
                } else if(row == mima.getLastAdress()){
                    setBackground(Colors.MIMA_TABLE_EXECUTED);
                    setForeground(Color.BLACK);
                } else if(row == mima.getLastChange()){
                    setBackground(Colors.MIMA_TABLE_LAST_MEM_CALL);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(Colors.MIMA_TABLE_DEFAULT);
                    setForeground(Colors.DEFAULT_COLOR);
                }
                return this;
            }
        });
        panel = new JScrollPane(table);
        panel.setPreferredSize(new Dimension(600,500));

    }

    public void revalidate(){
        listener.revalidate();

    }

    public TableListener getListener(){
        return listener;
    }

    public JScrollPane getPanel() {
        return panel;
    }

    public void clearComments(){
        for(int i = 0; i < shownLength; i++){
            table.getModel().setValueAt("",i,4);
        }
    }

    public String[] getComments(){
        String[] result = new String[shownLength];
        for(int i = 0; i < shownLength; i++){
            result[i] = ""+table.getModel().getValueAt(i,4);
        }
        return result;
    }

    public void setComment(String str, int adress){
        table.getModel().setValueAt(str,adress,4);
    }
}
