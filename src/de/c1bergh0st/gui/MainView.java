package de.c1bergh0st.gui;

import de.c1bergh0st.visual.HelpWindow;

import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import java.awt.ScrollPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;

public class MainView extends JPanel {
    public JButton buildBtn;
    public JButton disposeBtn;
    public JButton restoreBtn;
    public JButton lockBtn;
    public JButton stepBtn;
    public JButton runBtn;
    public JSpinner offsetSpinner;
    public JTextArea editor;
    public InputController contr;
    /**
     * Create the panel.
     */
    public MainView(InputController contr) {
        this.contr = contr;
        this.setPreferredSize(new Dimension(1000, 600));
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        JMenuBar menuBar = new JMenuBar();
        springLayout.putConstraint(SpringLayout.NORTH, menuBar, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, menuBar, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.EAST, menuBar, 0, SpringLayout.EAST, this);
        add(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmSave = new JMenuItem("Save");
        mnFile.add(mntmSave);

        JMenuItem mntmLoad = new JMenuItem("Load");
        mnFile.add(mntmLoad);

        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem mntmMiMaDoc = new JMenuItem("Mima Documentation");
        mnHelp.add(mntmMiMaDoc);

        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                HelpWindow w = new HelpWindow();
            }
        });
        mnHelp.add(mntmAbout);

        Panel bottomBar = new Panel();
        springLayout.putConstraint(SpringLayout.NORTH, bottomBar, -40, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.WEST, bottomBar, 0, SpringLayout.WEST, menuBar);
        springLayout.putConstraint(SpringLayout.SOUTH, bottomBar, 0, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, bottomBar, 0, SpringLayout.EAST, menuBar);
        add(bottomBar);
        bottomBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblProgrammOffset = new JLabel("Programm Offset in Memory:");
        bottomBar.add(lblProgrammOffset);

        offsetSpinner = new JSpinner();
        offsetSpinner .setModel(new SpinnerNumberModel(0, 0, 1048575, 1));
        bottomBar.add(offsetSpinner );

        buildBtn = new JButton("Build");
        buildBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                contr.build();
            }
        });
        bottomBar.add(buildBtn);

        disposeBtn = new JButton("Dispose");
        disposeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                contr.dispose();
            }
        });
        bottomBar.add(disposeBtn);
        disposeBtn.setEnabled(false);

        lockBtn = new JButton("Lock");
        lockBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                contr.lock();
            }
        });
        bottomBar.add(lockBtn);
        lockBtn.setEnabled(false);

        restoreBtn = new JButton("Restore");
        restoreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                contr.restore();
            }
        });
        bottomBar.add(restoreBtn);
        restoreBtn.setEnabled(false);

        stepBtn = new JButton("Step");
        stepBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                contr.step();
            }
        });
        bottomBar.add(stepBtn);
        stepBtn.setEnabled(false);

        runBtn = new JButton("Run");
        runBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                contr.run();
            }
        });
        bottomBar.add(runBtn);
        runBtn.setEnabled(false);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.5);
        springLayout.putConstraint(SpringLayout.NORTH, splitPane, 6, SpringLayout.SOUTH, menuBar);
        springLayout.putConstraint(SpringLayout.WEST, splitPane, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, splitPane, -6, SpringLayout.NORTH, bottomBar);
        springLayout.putConstraint(SpringLayout.EAST, splitPane, 0, SpringLayout.EAST, menuBar);
        add(splitPane);

        
        
        JPanel mimaPane = new JPanel();
        splitPane.setRightComponent(mimaPane);
        SpringLayout sl_mimaPane = new SpringLayout();
        mimaPane.setLayout(sl_mimaPane);

        editor = new JTextArea();
        splitPane.setLeftComponent(new CEditor(editor));
    }
}
