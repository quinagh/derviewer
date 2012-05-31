package demo.asn1viewer.ui;

import java.io.*;
import java.awt.*;
import javax.swing.*;

import demo.asn1viewer.ui.text.*;
import demo.asn1viewer.ui.internalframes.*;
import demo.asn1viewer.ui.tree.*;
import org.oregan.util.asn1.*;

public class TreeAndHexPanel extends JPanel
{
    private ASN1TreePanel jTree;
    private HexView funkyText;
    private File file;
    private byte[] bytes;

    public static OIDMapper oidmapper;


    public TreeAndHexPanel(File fname) throws IOException
    {
        this.file = fname;
        if (fname != null)
            init();
        else
        {
            add(new JLabel("Right-click on a project entry and select ASN.1 Viewer|View"));
        }
    }

    private void init() throws IOException
    {
        setLayout(new BorderLayout());
        FileInputStream in = new FileInputStream(file);
        bytes = new byte[(int) file.length()];

        in.read(bytes);

        funkyText = new HexView();
        funkyText.setBytes("Loading ...".getBytes());

        Runnable r = new Runnable()
        {
            public void run()
            {
                funkyText.setBytes(bytes);
            }
        };

        SwingUtilities.invokeLater(r);

        Runnable r2 = new Runnable()
        {
            public void run()
            {
                try
                {
                    //todo: this shouldn't run for _every_ instance...
                    DERViewer.oidmapper = new OIDMapper(new File("dumpasn1.cfg"));
                } catch (IOException e)
                {
                    System.err.println("Could not find dumpasn1.cfg");
                }
            }
        };

//        SwingUtilities.invokeLater(r2);

        TLV root = DERParser.decode(bytes);


        jTree = new ASN1TreePanel(root, funkyText);


        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        splitter.setResizeWeight(1);
        splitter.setLastDividerLocation(0);
        splitter.setOneTouchExpandable(true);
        splitter.setLeftComponent(jTree);

        JScrollPane jScrollPane = new JScrollPane(funkyText);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        jScrollPane.getVerticalScrollBar().setBlockIncrement(funkyText.getLineHeight() * 5);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(funkyText.getLineHeight() * Settings.getNumLineScroll());

        splitter.setRightComponent(jScrollPane);

        add(splitter, BorderLayout.CENTER);

        splitter.setDividerLocation(500);
        validate();
        setVisible(true);
        validate();
    }


    public void saveAs()
    {
        jTree.doSaveAs();
    }

    public void saveSelected()
    {
        jTree.doSaveSelected();
    }

    public String selectedToString()
    {
        return jTree.selectedToString();
    }
}


