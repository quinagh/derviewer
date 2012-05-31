package demo.asn1viewer;

import demo.asn1viewer.ui.TabbedTreeAndHexPane;

import javax.swing.*;
import java.io.File;
import java.awt.*;

public class Main extends JFrame
{
    private TabbedTreeAndHexPane mainPane;

    public Main()
    {
        super("DERViewer");

        mainPane = new TabbedTreeAndHexPane ();
        getContentPane ().setLayout (new BorderLayout(0,0));
        getContentPane ().add (mainPane,BorderLayout.CENTER);
    }


    public static void main(String[] args)
    {
        Main main = new Main();
        main.openFiles(args);
        main.pack();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }

    private void openFiles(final String[] args)
    {

        Runnable r = new Runnable(){
            public void run()
            {
                for (int i = 0; i < args.length; i++)
                {
                    String filename = args[i];
                    System.out.println("filename = " + filename);
                    mainPane.addFile(new File(filename));
                    invalidate();
                }
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
