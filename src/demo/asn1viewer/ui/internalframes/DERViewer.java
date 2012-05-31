package demo.asn1viewer.ui.internalframes;


import java.io.*;
import java.awt.*;
import javax.swing.*;

import demo.asn1viewer.ui.text.*;
import demo.asn1viewer.ui.tree.*;
import demo.asn1viewer.ui.*;
import org.oregan.util.asn1.*;

public class DERViewer extends JInternalFrame
{
    public static OIDMapper oidmapper;
    private TreeAndHexPanel treeAndHexPanel;

    public DERViewer(File filename) throws IOException
    {
        if (filename != null)
        {
            treeAndHexPanel = new TreeAndHexPanel(filename);
            getContentPane().add(treeAndHexPanel);
            setTitle(filename.getName());

        } else
        {
            add(new JLabel("Right-click on a project entry and select DER|View"));
        }

        invalidate();
        pack();

        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setClosable(true);

        setVisible(true);
        toFront();
    }

    public void saveAs()
    {
        treeAndHexPanel.saveAs();
    }

    public void saveSelected()
    {
        treeAndHexPanel.saveSelected();
    }

    public String selectedToString()
    {
        return treeAndHexPanel.selectedToString();
    }
}


