package demo.asn1viewer.layout;

import java.awt.event.*;
import java.awt.*;

import demo.asn1viewer.layout.*;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 14-May-2003
 * Time: 13:09:26
 * To change this template use Options | File Templates.
 */
public class LayoutTest
{
    public static void main(String args[])
    {
        // Create a frame
        Frame frame = new Frame("Example of TableLayout");
        frame.setBounds(100, 100, 300, 300);

        // Create a TableLayout for the frame
        double border = 10;
        double size[][] =
                {{border, 0.10, 20, TableLayout.FILL, 20, 0.20, border}, // Columns
                 {border, 0.20, 20, TableLayout.FILL, 20, 0.20, border}}; // Rows

        frame.setLayout(new TableLayout(size));

        // Create some buttons
        String label[] = {"Top", "Bottom", "Left", "Right", "Center", "Overlap"};
        Button button[] = new Button[label.length];

        for (int i = 0; i < label.length; i++)
            button[i] = new Button(label[i]);

        // Add buttons
        frame.add(button[0], "1, 1, 5, 1"); // Top
        frame.add(button[1], "1, 5, 5, 5"); // Bottom
        frame.add(button[2], "1, 3      "); // Left
        frame.add(button[3], "5, 3      "); // Right
        frame.add(button[4], "3, 3, c, c"); // Center
        frame.add(button[5], "3, 3, 3, 5"); // Overlap

        // Allow user to close the window to terminate the program
        frame.addWindowListener
                (new WindowListener()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        System.exit(0);
                    }

                    public void windowOpened(WindowEvent e)
                    {
                    }

                    public void windowClosed(WindowEvent e)
                    {
                    }

                    public void windowIconified(WindowEvent e)
                    {
                    }

                    public void windowDeiconified(WindowEvent e)
                    {
                    }

                    public void windowActivated(WindowEvent e)
                    {
                    }

                    public void windowDeactivated(WindowEvent e)
                    {
                    }
                }
                );

        // Show frame
        frame.show();
    }

}
