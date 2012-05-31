/*
 * @(#)ULineBorder.java	1.22 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package demo.asn1viewer.ui;

import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Component;

/**
 * A class which implements a line border of arbitrary thickness
 * and of a single color.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @version 1.22 01/23/03
 * @author David Kloba
 */
public class ULineBorder extends AbstractBorder
{
    private static Border blackLine;
    private static Border grayLine;

    protected int thickness;
    protected Color lineColor;
    protected boolean roundedCorners;
    private String sides = "nsew";

    /** Convenience method for getting the Color.black ULineBorder of thickness 1.
     */
    public static Border createBlackLineBorder()
    {
        if (blackLine == null)
        {
            blackLine = new ULineBorder(Color.black, 1);
        }
        return blackLine;
    }

    /** Convenience method for getting the Color.gray ULineBorder of thickness 1.
     */
    public static Border createGrayLineBorder()
    {
        if (grayLine == null)
        {
            grayLine = new ULineBorder(Color.gray, 1);
        }
        return grayLine;
    }

    /**
     * Creates a line border with the specified color and a
     * thickness = 1.
     * @param color the color for the border
     */
    public ULineBorder(Color color)
    {
        this(color, 1, false);
    }

    /**
     * Creates a line border with the specified color and thickness.
     * @param color the color of the border
     * @param thickness the thickness of the border
     */
    public ULineBorder(Color color, int thickness)
    {
        this(color, thickness, false);
    }

    public ULineBorder(Color color, String sides, boolean rounded)
    {
        this(color, sides);
        roundedCorners = rounded;
    }

    public ULineBorder(Color color, String sides)
    {
        this(color, 1, false);
        this.sides = sides;
    }

    /**
     * Creates a line border with the specified color, thickness,
     * and corner shape.
     * @param color the color of the border
     * @param thickness the thickness of the border
     * @param roundedCorners whether or not border corners should be round
     * @since 1.3
     */
    public ULineBorder(Color color, int thickness, boolean roundedCorners)
    {
        lineColor = color;
        this.thickness = thickness;
        this.roundedCorners = roundedCorners;
    }

    /**
     * Paints the border for the specified component with the
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
//        System.out.println(width + "x" + height);
        Color oldColor = g.getColor();
        int i;

        /// PENDING(klobad) How/should do we support Roundtangles?
        g.setColor(lineColor);
        for (i = 0; i < thickness; i++)
        {
            int edgepad = 0;
            if(roundedCorners)
                edgepad = 1;
            if (sides.indexOf('n') >= 0)
                g.drawLine(x+edgepad, y + i, (x) + (width - edgepad - 1), y + i);
            if (sides.indexOf('s') >= 0)
            {
                int X1 = x+edgepad;
                int Y1 = y + height - i - 1;
                int X2 = x + width - 1 - edgepad;
                int Y2 = y + (height - i - 1);
                g.drawLine(X1, Y1, X2, Y2);
            }
            if (sides.indexOf('e') >= 0)
            {
                g.drawLine(x + width - 1, y, x + width - 1, y + height -edgepad- 1);
            }
            if (sides.indexOf('w') >= 0)
            {
                g.drawLine(x, y, x, y + height -edgepad - 1);
            }

        }
        g.setColor(oldColor);
    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    public Insets getBorderInsets(Component c)
    {
        Insets insets = new Insets(0, thickness, thickness, thickness);

        if (sides.indexOf('n') == -1)
            insets.top = 0;
        return insets;
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets)
    {
        insets.left = insets.right = insets.bottom = thickness;
        if (sides.indexOf('n') == -1)
            insets.top = 0;
        return insets;
    }

    /**
     * Returns the color of the border.
     */
    public Color getLineColor()
    {
        return lineColor;
    }

    /**
     * Returns the thickness of the border.
     */
    public int getThickness()
    {
        return thickness;
    }

    /**
     * Returns whether this border will be drawn with rounded corners.
     */
    public boolean getRoundedCorners()
    {
        return roundedCorners;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    public boolean isBorderOpaque()
    {
        return !roundedCorners;
    }

}
