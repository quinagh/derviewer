package demo.asn1viewer;

import demo.asn1viewer.ui.ULineBorder;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 23-Jul-2003
 * Time: 18:29:13
 * To change this template use Options | File Templates.
 */
public class foobar
{
    public static void main(String[] args)
    {
        JFrame f = new JFrame("Test");

        JPanel panel = new JPanel(new BorderLayout(0,0));
        panel.setOpaque(false);
        panel.setBorder(new ULineBorder(Color.red,"nsew"));
        panel.setPreferredSize(new Dimension(100,100));
        panel.add(new JButton("Hello"),BorderLayout.CENTER);
        f.getContentPane().add(panel);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }
}
