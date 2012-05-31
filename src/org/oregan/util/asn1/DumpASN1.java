package org.oregan.util.asn1;

import java.io.*;

public class DumpASN1
{
    public static void main(String[] args) throws IOException
    {
        File f = new File(args[0]);
        byte[] bytes = new byte[(int) f.length()];
        FileInputStream fis = new FileInputStream(f);
        fis.read(bytes);
        fis.close();

        doIt(bytes);


    }

    public static void doIt(byte[] bytes)
    {
        long start = System.currentTimeMillis();
        TLV rootnode = DERParser.decode(bytes);

        System.out.println("Parsed in " + (System.currentTimeMillis() - start) + "ms");

        MyPrintingVisitor mpv = new MyPrintingVisitor();

        mpv.traverse(rootnode);

//        TLV certificate = new TLV(ASN1.SEQUENCE);
//        certificate.addChild(new TLV(ASN1.SEQUENCE));
//        TLV algID = new TLV(ASN1.SEQUENCE);
//        algID.addChild(new TLV(ASN1.OBJECTIDENTIFIER));
//        algID.addChild(new TLV(ASN1.SEQUENCE, true));
//        certificate.addChild(algID);
//        certificate.addChild(new TLV(ASN1.BITSTRING));
//
//        boolean result = match(certificate, rootnode);
//
//        System.out.println("result = " + result);





        System.out.println(mpv.buffer);
    }

    private static boolean match(TLV lhs, TLV rhs)
    {
        if (lhs.getTag() == lhs.getTag())
        {
            for (int i = 0; i < lhs.getNumChildren(); i++)
            {
                TLV child = lhs.getChild(i);
                if (!match(child, rhs.getChild(i)) && !child.isOptional())
                    return false;
            }
            return true;
        }
        return false;
    }
}

class MyPrintingVisitor //implements Visitor
{
    public StringBuffer buffer = new StringBuffer();

    private int depth = 0;


    public void traverse(TLV rootnode)
    {
        int numChildren = rootnode.getNumChildren();
        buffer.append(padInteger(rootnode.getTagOffset()) +
                " " +
                padInteger(rootnode.getLength()) +
                " " +
                println(toDisplayableTag(rootnode) + (numChildren > 0 ? "{" : "")));

        for (int i = 0; i < numChildren; i++)
        {
            depth++;
            traverse(rootnode.getChild(i));
            depth--;
        }
        if (numChildren > 0)
            buffer.append("            " + println("}"));
    }

    private String toDisplayableTag(TLV rootnode)
    {
        byte tag = rootnode.getValueRef()[rootnode.getTagOffset()];
        switch (tag)
        {
            case 0x01:  // Boolean
                return "BOOLEAN";
            case 0x02:  // Integer
                return "INTEGER";
            case 0x23:	// Constructed BitString
            case 0x03:  // Bit String
                return "BITSTRING";
            case 0x24:	// Constructed Octet String
            case 0x04:  // Octet String
                return "OCTETSTRING";

            case 0x05:  // Null
                return "NULL";

            case 0x06:  // Object Identifier
                return "OBJECTIDENTIFIER";

            case 0x0a:  // Enumerated
                return "ENUMERATED";

            case 0x12:  // Numeric String
                return "NUMERICSTRING";
            case 0x13:  // Printable String
                return "PRINTABLESTRING";
            case 0x14:  // T61 String

                return "T61STRING";
            case 0x16:  // IA5 String
                return "IA5STRING";
            case 0x1a:  // Visible String
                return "VISIBLE STRING";
            case 0x0c:
            case 0x1c:  // Universal String
                return "UNIVERSAL STRING";
            case 0x1e:  // BMP String
                return "BMP STRING";
            case 0x17:  // UTC Time
                return "UTC TIME";
            case 0x18:  // Generalized Time
                return "GENERALISEDTIME";
            case 0x30:  // SEQUENCE (or SEQUENCE OF)
                return "SEQUENCE";
            case 0x31:
                return "SETOF";
            default:
                {
                    if ((tag & 0x80) == 0x80)
                    {
                        return ("CONTEXTSPECIFIC[" + (tag & 0x1f) + "]");
                    } else
                        return "0x" + Integer.toHexString(tag);
                }
        }

    }

    private static final String spaces = "                                                                                                       ";
    private static final String zeros = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";


    private String println(String message)
    {
        String ret = "";
        ret = padInteger(depth);
        ret += spaces.substring(0, (depth * 2) + 1);
        ret += message + "\n";
        return ret;
    }

    private String padInteger(int integer)
    {
        String depthStr = Integer.toString(integer);
        String ret = zeros.substring(0, 10 - depthStr.length());
        ret += depthStr;
        return ret;
    }


    public void visit(TLV node)
    {

    }


}
