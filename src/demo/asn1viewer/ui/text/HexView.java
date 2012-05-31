package demo.asn1viewer.ui.text;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;

import org.oregan.util.*;
import org.oregan.util.asn1.*;
import demo.asn1viewer.layout.*;

public class HexView extends JPanel implements TreeSelectionListener
{
    public final String CRLF = "\r\n";
    private static final String zeros = "00000000";

    public Color IDEA_HIGHLIGHT_COLOR = new Color(255, 255, 215);
    public Color BLUE_HIGHLIGHT_COLOR = new Color(153, 153, 255);

    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(BLUE_HIGHLIGHT_COLOR);

    private JTextArea main;
    private JTextArea loc;
    private JTextArea txtView;
    private Font monospaced;
    private double[][] doubles;
    private TableLayout tl;
    public Dimension minimumBounds;

    public HexView()
    {

        init();
    }

    private void init()
    {

        this.setOpaque(true);
        this.setBackground(Color.white);

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Hex View", TitledBorder.LEADING, TitledBorder.TOP));
        main = new JTextArea();
        loc = new JTextArea();
        txtView = new JTextArea();

        monospaced = new Font("monospaced", Font.PLAIN, 12);
        main.setFont(monospaced);
        loc.setFont(monospaced);
        txtView.setFont(monospaced);

        loc.setColumns(8);
        main.setColumns(47);
        txtView.setColumns(16);

        loc.setRows(40);
        main.setRows(40);
        txtView.setRows(40);

        loc.setEditable(false);
//        loc.setBackground(Color.red);
        main.setEditable(false);
//        main.setBackground(Color.green);
        txtView.setEditable(false);
//        txtView.setBackground(Color.blue);

        loc.validate();
        int locWidth = loc.getPreferredSize().width;
        main.validate();
        int mainWidth = main.getPreferredSize().width;
        txtView.validate();
        int txtWidth = txtView.getPreferredSize().width;

        int txtHeight = txtView.getPreferredSize().height;

//        System.out.print("|-" + locWidth+"-+-"+ mainWidth+"-+-"+ txtWidth+"-|");
//        System.out.println("txtHeight = " + txtHeight);


        doubles = new double[][]{{locWidth,10, mainWidth,10, txtWidth,TableLayout.FILL}, // cols
                                 {TableLayout.FILL}}; // rows

        tl = new TableLayout(doubles);
        setLayout(tl);

        add(loc, "0,0 ");
        add(main, "2,0 ");
        add(txtView, "4,0 ");

//        JScrollBar bar = new JScrollBar(JScrollBar.VERTICAL);
//        bar.setBlockIncrement(getLineHeight()*5);
//        bar.setUnitIncrement(getLineHeight());
//        add(bar,"5,0");

        invalidate();
        repaint();

    }

    public int getLineHeight()
    {
        FontMetrics fm = main.getFontMetrics(monospaced);
        return fm.getMaxAscent() + fm.getMaxDescent();
    }

    public boolean isMinimumSizeSet()
    {
        return true;
    }

//    public Dimension getMinimumSize()
//    {
//        if (minimumBounds == null)
//        {
//            FontMetrics fm = main.getFontMetrics(monospaced);
//            Rectangle2D rect = fm.getStringBounds(new char[75], 0, 75, getGraphics());
//            minimumBounds = new Dimension((int) rect.getWidth(), getLineHeight() * 5);
//        }
//        return minimumBounds;
//    }
//
//    public Dimension getPreferredSize()
//    {
//        Dimension min = this.getMinimumSize();
//        min.setSize(min.getWidth(), getLineHeight() * 15);
////        System.out.println("perferred = " + min);
//        return min;
//    }

    public void setBytes(byte[] bytes)
    {

        int lineLength = 16;

        int numLines = (bytes.length / lineLength) + 1;


        //        if (bytes.length % lineLength != 0)
        //            numLines += 1;
        //        System.out.println("lines = " + numLines);
        StringBuffer locBuf = new StringBuffer();
        StringBuffer mainBuf = new StringBuffer();
        StringBuffer txtBuf = new StringBuffer();

        for (int i = 0; i < numLines; i++)
        {
            int startOfLine = i * lineLength;
            int length = lineLength;
            if (i == numLines - 1)
            {
                length = bytes.length % lineLength;
            }
            if (length > 0)
            {
                String indexStr = Integer.toHexString(startOfLine);
                locBuf.append(zeros.substring(0, 8 - indexStr.length()));
                locBuf.append(indexStr);

                mainBuf.append(Util.toHexString(bytes, startOfLine, length, true));
                for (int j = 0; j < length; j++)
                {
                    char c = (char) bytes[startOfLine + j];
                    if (c > ' ' && c < '~')
                        txtBuf.append(c);
                    else
                        txtBuf.append('.');
                }
            }

            locBuf.append(CRLF);
            mainBuf.append(CRLF);
            txtBuf.append(CRLF);

        }

        this.loc.setText(locBuf.toString());
        this.main.setText(mainBuf.toString());
        this.txtView.setText(txtBuf.toString());

    }

    public void valueChanged(TreeSelectionEvent e)
    {

        JTree source = (JTree) e.getSource();

        TLV tlv = ((TLV) ((DefaultMutableTreeNode) source.getSelectionPath().getLastPathComponent()).getUserObject());

        highlightTLV(tlv);

    }

    public void highlightTLV(TLV tlv)
    {
        int start = tlv.getTagOffset();
        int end = (tlv.getValueOffset() + tlv.getLength());

        int mainStart = calcOffsetInMain(start, main, true);
        int mainEnd = calcOffsetInMain(end, main, true) - 1;
        removeHighlights(main);
        applyHighlights(main, mainStart, mainEnd);

        int txtStart = calcOffsetInMain(start, txtView, false);
        int txtEnd = calcOffsetInMain(end, txtView, false);
        removeHighlights(txtView);
        applyHighlights(txtView, txtStart, txtEnd);
    }

    private int calcOffsetInMain(int start, JTextArea text, boolean isHex)
    {

        int numLines = (start / 16);
        int lineOffset = start % 16;

        int offset = numLines * (text.getColumns() + 2);

        offset += (lineOffset * (isHex ? 2 : 0) + lineOffset);

        return offset;
    }

    private void applyHighlights(JTextArea comp, int start, int end)
    {
        Highlighter hilite = comp.getHighlighter();
        try
        {
            hilite.addHighlight(start, end, myHighlightPainter);
        } catch (BadLocationException e)
        {
//            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        try
        {
            comp.setCaretPosition(end);
        } catch (Exception e)
        {

        }
        comp.setCaretPosition(start);
    }

    public void removeHighlights()
    {
        removeHighlights(main);
        removeHighlights(txtView);
    }

    // Removes only our private highlights
    private void removeHighlights(JTextArea comp)
    {
        Highlighter hilite = comp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++)
        {
            if (hilites[i].getPainter() instanceof MyHighlightPainter)
            {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }

}

// A private subclass of the default highlight painter

class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
{
    public MyHighlightPainter(Color color)
    {
        super(color);
    }
}

