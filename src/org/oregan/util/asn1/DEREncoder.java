package org.oregan.util.asn1;

import org.oregan.util.Util;

import java.util.Stack;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DEREncoder
{
    public static byte[] encode(TLV root)
    {
        Stack st = new Stack();
        Stack st2 = new Stack();
        int len = traverse2(root, st);
        System.out.println("len = " + len);


        while (!st.isEmpty())
        {
            st2.push(st.pop());
        }

        traverse3(st2);

        return null;

    }

    private static void traverse3(Stack st)
    {
        TLV tlv = (TLV) st.pop();
        byte[] value = tlv.getValue();
        System.out.println("tlv.getTag() = " + tlv.getTag() + Util.toHexString(value, 0, value.length) + "---" );
        System.out.println("{");
        while(tlv != ((TLV)st.peek()))
        {
            traverse3(st);
        }
        st.pop();
        System.out.println("} " + "tlv.getTag() = " + tlv.getTag());
    }


    private static int traverse2(TLV node, Stack st)
    {
        st.push(node);
        int numChildren = node.getNumChildren();
        for (int i = numChildren-1; i >=0;i--)
        {
            traverse2(node.getChild(i), st);
        }
        st.push(node);
        return numChildren;
    }

    private static int traverse(TLV node, Stack st)
    {
        int encodedLen = 0;

        if (node.getNumChildren() != 0)
        {
            int stackSize = st.size();
            for (int i = 0; i < node.getNumChildren(); i++)
            {
                encodedLen += traverse(node.getChild(i), st);
            }
            int newStackSize = st.size();

            int length = encodedLen;
            byte[] myLength = ASN1.encodeLength(length);
            byte[] value = new byte[1 + myLength.length + length];
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
                for (int i = 0; i < newStackSize - stackSize; i++)
                {
                    byte[] tmp = (byte[]) st.pop();
                    baos.write(tmp);
                }
                byte[] done = baos.toByteArray();
                System.arraycopy(done, 0, value, 1 + myLength.length, length);
                System.arraycopy(myLength, 0, value, 1, myLength.length);
                value[0] = node.getTag();
                st.push(value);

            } catch (IOException e)
            {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }


        } else
        {
            int length = node.getLength();
            encodedLen += (1 + node.getLengthOfLength() + length);
            byte[] encoded = new byte[encodedLen];
            byte tag = node.getTag();
            encoded[0] = tag;
            System.out.println("Tag" + Integer.toString(tag, 16) + encodedLen);
            byte[] len = ASN1.encodeLength(length);
            System.arraycopy(len, 0, encoded, 1, len.length);
            System.arraycopy(node.getValue(), 0, encoded, 1 + len.length, node.getLength());
            st.push(encoded);
        }


        return encodedLen;
    }


    public static void main(String[] args)
    {

        TLV root = new TLV(ASN1.SEQUENCE);
        TLV int1 = new TLV(ASN1.INTEGER);
        int1.setValue(Util.toBytes(1));
        TLV int2 = new TLV(ASN1.INTEGER);
        int2.setValue(Util.toBytes(2));
        root.addChild(int1);
        root.addChild(int2);

        TLV seq = new TLV(ASN1.SEQUENCE);
                TLV int3 = new TLV(ASN1.INTEGER);
                int3.setValue(Util.toBytes(3));
                TLV int4 = new TLV(ASN1.INTEGER);
                int4.setValue(Util.toBytes(4));
                seq.addChild(int3);
                seq.addChild(int4);


        root.addChild(seq);

        TLV int5 = new TLV(ASN1.INTEGER);
        int5.setValue(Util.toBytes(5));
        root.addChild(int5);


        byte[] encoded = DEREncoder.encode(root);


    }
}
