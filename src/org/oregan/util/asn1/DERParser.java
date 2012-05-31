package org.oregan.util.asn1;

/**
 * Parses DER into a TLV tree.
 */
public class DERParser
{

    public static TLV decode(byte[] array)
    {
        TLV rootnode = new TLV();
        rootnode.setValueRef(array);
        rootnode.setTagOffset(0);
        rootnode.setLengthOffset(1);
        rootnode.setValueOffset(rootnode.getLengthOffset() + rootnode.getLengthOfLength());

        int startingAt = rootnode.getValueOffset();

        while ((startingAt = DERParser.look(rootnode, startingAt)) != -1) ;

        return rootnode;
    }

    private static int look(TLV parent, int startingAt)
    {
        int offset = startingAt;

        TLV thisNode = new TLV();
        try
        {
            if (startingAt >= (parent.getValueOffset() + parent.getLength()))
            {
                offset = -1;
                return offset;
            }
            byte[] bytesRef = parent.getValueRef();

            thisNode.setValueRef(bytesRef);

            thisNode.setTagOffset(offset);
            offset++;
            thisNode.setLengthOffset(offset);

            int length = thisNode.getLength();
            int lengthOfLength = thisNode.getLengthOfLength();
            int totalLength = 1 + lengthOfLength + length;
            thisNode.setValueOffset(offset + lengthOfLength);

            offset += lengthOfLength;

            parent.addChild(thisNode);

            // ---

            while (offset != -1)
            {
                if (maybeDecodable(thisNode, false))
                {
                    offset = look(thisNode, offset);
                } else
                {
                    offset = -1;
                }
            }

            // ---
            // never go further than our own bounds....
            offset = startingAt + totalLength;
            return offset;
        } finally
        {

        }
    }

    private static boolean maybeDecodable(TLV root, boolean simple)
    {
        boolean canOpen = false;

        byte b = root.getValueRef()[root.getTagOffset()];

        if (b == 0x04)
        {
            canOpen = simple ? true : checkOpenable(root);

        } else if (b == 0x24)
        {
            canOpen = simple ? true : checkOpenable(root);
        } else if (((b & 0x80) == 0x80) && (b & 0x20) == 0x20)
        {
            canOpen = simple ? true : checkOpenable(root);

        } else if (b == 0x03)
        {
            canOpen = simple ? true : checkOpenable(root);
        } else if (!isLeaf(root))
        {
            canOpen = true;
        }
        return canOpen;
    }

    public static boolean checkOpenable(TLV root)
    {
        int lol = ASN1.getLengthOfLength(root.getValueRef(), root.getLengthOffset());
        int len = ASN1.getLength(root.getValueRef(), root.getLengthOffset());

        TLV tmpTLV = new TLV(true);

        tmpTLV.setValueRef(root.getValueRef());
        tmpTLV.setTagOffset(root.getLengthOffset() + lol);


        boolean openable = (maybeDecodable(tmpTLV, true) | isLeaf(tmpTLV));

        if (openable)
        {
            int innerOffset = lol + 1;
            int innerLengthOffset = root.getTagOffset() + innerOffset + 1;
            int innerLOL = ASN1.getLengthOfLength(root.getValueRef(), innerLengthOffset);
            int innerLen = ASN1.getLength(root.getValueRef(), innerLengthOffset);

            if ((len - innerOffset) == innerLen)
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isLeaf(TLV node)
    {
        byte tag = node.getValueRef()[node.getTagOffset()];
        switch (tag)
        {
            case 0x30:
            case 0x31:
                return false;
            default:
                return true;
        }
    }
}
