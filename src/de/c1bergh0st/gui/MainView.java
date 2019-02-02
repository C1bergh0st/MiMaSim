package de.c1bergh0st.gui;

import de.c1bergh0st.visual.HelpWindow;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.text.StyledDocument;

public class MainView extends JPanel {
    public JButton buildBtn;
    public JButton disposeBtn;
    public JSpinner offsetSpinner;
    public JTextPane editor;
    public InputController contr;
    public JSplitPane splitPane;
    public StyledDocument styledDoc;
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
        mntmSave.addActionListener(ev -> {
            contr.save();
        });
        mnFile.add(mntmSave);

        JMenuItem mntmLoad = new JMenuItem("Load");
        mntmLoad.addActionListener(ev -> {
            contr.load();
        });
        mnFile.add(mntmLoad);

        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem mntmMiMaDoc = new JMenuItem("Mima Documentation");
        mntmMiMaDoc.addActionListener(ev -> {
            //If possible, open the Link
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("http://ti.ira.uka.de/Visualisierungen/Mima/mima-aufgaben.pdf"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        mnHelp.add(mntmMiMaDoc);


        JMenuItem mntmDonate = new JMenuItem("Donate");
        mntmDonate.addActionListener(ev -> {
            //If possible, open the Link
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.paypal.me/c1bergh0st"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        mnHelp.add(mntmDonate);

        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(ev -> {
            HelpWindow w = new HelpWindow();
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
        buildBtn.addActionListener(ev -> contr.build());
        bottomBar.add(buildBtn);

        disposeBtn = new JButton("Dispose");
        disposeBtn.addActionListener(ev -> contr.dispose());
        bottomBar.add(disposeBtn);
        disposeBtn.setEnabled(false);



        splitPane = new JSplitPane();
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

        editor = new JTextPane();
        editor.setFont(new Font("Monospaced",Font.PLAIN,14));

        styledDoc = editor.getStyledDocument();
        splitPane.setLeftComponent(new CEditor(editor));
    }
}
