//Copyright (C) 2018  Philipp Berdesinski
// A MiMa Simulator with GUI
// The Copyright outlined in the File LICENSE applies

package de.c1bergh0st.visual;

import javax.swing.*;
import java.awt.*;

public class HelpWindow extends JFrame {
    private String info = "<html><table style=\"width: 417px;\">\n" +
            "<tbody>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">&nbsp;Binary</td>\n" +
            "<td style=\"width: 55px;\">\"Code\"&nbsp;</td>\n" +
            "<td style=\"width: 309px;\">Meaning&nbsp;</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">0000</td>\n" +
            "<td style=\"width: 55px;\">LDC c</td>\n" +
            "<td style=\"width: 309px;\">&nbsp;c &rarr; Akku</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">0001&nbsp;</td>\n" +
            "<td style=\"width: 55px;\">LDV a&nbsp;</td>\n" +
            "<td style=\"width: 309px;\">&nbsp;&lt;a&gt;&nbsp;&rarr; Akku</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">0010&nbsp;</td>\n" +
            "<td style=\"width: 55px;\">STV a&nbsp;</td>\n" +
            "<td style=\"width: 309px;\">Akku &rarr; &lt;a&gt;</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">0011&nbsp;</td>\n" +
            "<td style=\"width: 55px;\">ADD a&nbsp;</td>\n" +
            "<td style=\"width: 309px;\">Akku + &lt;a&gt;&nbsp; &rarr; Akku&nbsp;</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">0100&nbsp;</td>\n" +
            "<td style=\"width: 55px;\">AND a</td>\n" +
            "<td style=\"width: 309px;\">Akku AND &lt;a&gt;&nbsp; &rarr; Akku&nbsp;</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">0101&nbsp;</td>\n" +
            "<td style=\"width: 55px;\">OR a&nbsp;</td>\n" +
            "<td style=\"width: 309px;\">Akku OR &lt;a&gt; &rarr; Akku</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">0110&nbsp;</td>\n" +
            "<td style=\"width: 55px;\">XOR a&nbsp;</td>\n" +
            "<td style=\"width: 309px;\">Akku XOR &lt;a&gt; &rarr; Akku&nbsp;</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">0111</td>\n" +
            "<td style=\"width: 55px;\">EQL a</td>\n" +
            "<td style=\"width: 309px;\">if(Akku == &lt;a&gt;) { -1&nbsp; &rarr; Akku;} else { 0 &rarr; Akku;}</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">1000</td>\n" +
            "<td style=\"width: 55px;\">JMP a</td>\n" +
            "<td style=\"width: 309px;\">Sprung zu Adresse a</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">1001</td>\n" +
            "<td style=\"width: 55px;\">JMN a</td>\n" +
            "<td style=\"width: 309px;\">Wenn Akku &lt; 0 dann Sprung zu Adresse a</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">1010</td>\n" +
            "<td style=\"width: 55px;\">LDIV a</td>\n" +
            "<td style=\"width: 309px;\">&lt;&lt;a&gt;&gt;&nbsp;&rarr; Akku</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">1011</td>\n" +
            "<td style=\"width: 55px;\">STIV a</td>\n" +
            "<td style=\"width: 309px;\">Akku &rarr; &lt;&lt;a&gt;&gt;</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">1100</td>\n" +
            "<td style=\"width: 55px;\">RAR</td>\n" +
            "<td style=\"width: 309px;\">Rotiert den Akku eins nach rechts (und das rechteste Bit an die linkeste Stelle)</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">1101</td>\n" +
            "<td style=\"width: 55px;\">NOT</td>\n" +
            "<td style=\"width: 309px;\">Invertiert jedes Bit des Akku</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">1110</td>\n" +
            "<td style=\"width: 55px;\">EMPTY</td>\n" +
            "<td style=\"width: 309px;\">Unbenutzt, eventuell sp&auml;ter JIND a</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"width: 51px;\">1111</td>\n" +
            "<td style=\"width: 55px;\">HALT</td>\n" +
            "<td style=\"width: 309px;\">Stoppt die Mima</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "<!-- DivTable.com --></html>";
    public HelpWindow(){
        super("HELP");
        JPanel panel = new JPanel();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //panel.setPreferredSize(new Dimension(300,200));
        panel.add(new JLabel(info));
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}
