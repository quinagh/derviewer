package demo.asn1viewer.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 16-Jul-2003
 * Time: 19:04:03
 * To change this template use Options | File Templates.
 */
public class TabButton extends JButton
{
    private ULineBorder unSelectedLineBorder = new ULineBorder(Color.gray,"esw");
    private ULineBorder selectedLineBorder = new ULineBorder(Color.gray, "esw", true);

    private Color selectedBg;
    private Color unSelectedBg;


    public TabButton(String text)
    {
        super(text);
        selectedBg = getBackground().brighter();
        unSelectedBg = getBackground();

        setOpaque(true);
        setBackground(unSelectedBg);
        setBorderPainted(true);
        setBorder(unSelectedLineBorder);
    }

    public void setSelected(boolean selected)
    {
//        setOpaque(selected);
        setBorder((selected?(Border)selectedLineBorder:(Border)unSelectedLineBorder));
        setBackground((selected?selectedBg:unSelectedBg));
        invalidate();
        repaint();
    }

    public Dimension getPreferredSize()
    {
        Dimension preferredSize = super.getPreferredSize();
        Dimension biggerer = new Dimension((int)(preferredSize.getWidth()*1.2), (int)(preferredSize.getHeight()*1.2));
        return biggerer;    //To change body of overriden methods use Options | File Templates.
    }

}
