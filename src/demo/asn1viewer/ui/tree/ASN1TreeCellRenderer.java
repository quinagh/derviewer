package demo.asn1viewer.ui.tree;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;

import demo.asn1viewer.ui.*;
import demo.asn1viewer.ui.internalframes.*;
import demo.asn1viewer.*;
import demo.asn1viewer.util.*;
import org.oregan.util.*;
import org.oregan.util.asn1.*;

public class ASN1TreeCellRenderer extends DefaultTreeCellRenderer
{

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        Component treeCellRendererComponent = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (treeCellRendererComponent instanceof JLabel)
        {
            JLabel label = (JLabel) treeCellRendererComponent;
            Object userObject = node.getUserObject();

            if (userObject instanceof TLV)
            {
                TLV rootnode = (TLV) userObject;
                String tagName = TagToString.toDisplayableTag(rootnode, false);

                byte tag = rootnode.getValueRef()[rootnode.getTagOffset()];
                if (tag == 0x30 && !expanded)
                    tagName += " " + rootnode.getNumChildren() + " children";
                label.setText(tagName);
                label.setToolTipText(tagName);
                ToolTipManager.sharedInstance().registerComponent(label);



            }
        }
        treeCellRendererComponent.invalidate();

        return treeCellRendererComponent;
    }

}
