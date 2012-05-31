package demo.asn1viewer.ui;

import java.util.*;

import org.oregan.util.asn1.*;
import demo.asn1viewer.ui.tree.*;
import demo.asn1viewer.*;
import demo.asn1viewer.util.*;

public class MyPrintingVisitor implements Visitor
{
    public StringBuffer buffer = new StringBuffer();

    private int depth = 0;


    public void traverse(TLV rootnode)
    {
        rootnode.accept(this);

        Enumeration en = rootnode.enumeration();
        boolean b = en.hasMoreElements();
        if (b)
            buffer.append(println(-1, "{"));
        while (en.hasMoreElements())
        {

            depth++;
            traverse((TLV) en.nextElement());
            depth--;

        }
        if (b)
            buffer.append(println(-1, "}"));
    }

    private static final String spaces = "                                                                                                       ";
    private static final String zeros = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";


    private String println(int offset, String message)
    {
        String ret = "";
        if (offset != -1)
        {
            String offsetStr = Integer.toHexString(offset);
            ret += zeros.substring(0, 8 - offsetStr.length());
            ret += offsetStr;
        } else
        {
            ret += spaces.substring(0, 8);
        }
        ret += spaces.substring(0, depth * 2);
        ret += "  " + message + "\n";
        return ret;
    }


    public void visit(TLV node)
    {
        String desc = TagToString.toDisplayableTag(node, true);
        String line = println(node.getTagOffset(), desc);
        buffer.append(line);

    }


}
