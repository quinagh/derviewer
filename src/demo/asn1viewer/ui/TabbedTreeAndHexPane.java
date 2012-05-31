package demo.asn1viewer.ui;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 17-Jul-2003
 * Time: 07:07:33
 * To change this template use Options | File Templates.
 */
public class TabbedTreeAndHexPane extends TabbedPane
{
    boolean empty = true;
    JLabel info = new JLabel("Use ASN.1 Viewer| View from project pane");

    public TabbedTreeAndHexPane()
    {
        panes.add("Information", info);
    }

    public TabbedTreeAndHexPane(File file)
    {
        addFile(file);
    }

    public void addFile(File file)
    {
        if(empty)
        {
            remove(info);
            empty = false;
        }

        try
        {
            addTab(file.getName(),new TreeAndHexPanel(file));
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
    }
}
