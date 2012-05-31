package org.oregan.util.asn1;


import java.util.*;

public class TLV implements Vistable
{
    private static int instanceCount = 0;

    private int instanceID = 0;

    private int tagOffset = 0;

    private int lengthOffset = 0;

    private int valueOffset = 0;

    private byte[] valueRef;

    private byte[] value = new byte[0];

    private int length = -1;

    private int lengthOfLength = -1;


    private boolean optional = false;

    private Vector children = new Vector();
    private byte tag;

    public TLV()
    {
        this(false);
    }

    public TLV(boolean temporaryTLV)
    {
        if (temporaryTLV)
            instanceID = -instanceCount;
        else
            instanceID = instanceCount++;
    }

    public TLV(byte tag)
    {
        this(tag, false);
    }

    public TLV(byte tag, boolean optional)
    {
        this.tag = tag;
        setOptional(optional);
    }

    public boolean isOptional()
    {
        return optional;
    }

    public void setOptional(boolean optional)
    {
        this.optional = optional;
    }


    public void addChild(TLV sprog)
    {
        children.addElement(sprog);
    }

    public TLV getChild(int index)
    {
        return (TLV) children.elementAt(index);
    }

    public int getNumChildren()
    {
        return children.size();
    }

    public Enumeration enumeration()
    {
        return children.elements();
    }


    public int getLength()
    {
        if (length == -1)
            calcLengths();
        return length;
    }

    public int getLengthOfLength()
    {
        if (lengthOfLength == -1)
            calcLengths();
        return lengthOfLength;
    }

    private void calcLengths()
    {
        if(getValueRef()!=null){
        lengthOfLength = ASN1.getLengthOfLength(getValueRef(), getLengthOffset());
        length = ASN1.getLength(getValueRef(), getLengthOffset());
        }
        else{
            System.out.println("------" + Integer.toHexString(tag));
            length = getValue().length;
            lengthOfLength = ASN1.encodeLength(length).length;


        }
    }

    public String toString()
    {
        return "TLV [[0x" + Integer.toHexString(getTag()) + "][" + getLength() + "][..]@" + instanceID + "]";

    }

    public int hashCode()
    {
        return getValueRef().hashCode() ^ getTagOffset();
    }


    public boolean equals(Object obj)
    {
        TLV rhs = (TLV) obj;
        return org.oregan.util.Util.cmpByteArrays(getValueRef(), getTagOffset(), rhs.getValueRef(), rhs.getTagOffset(), 1 + rhs.getLengthOfLength() + rhs.getLength());
    }

    public int getTagOffset()
    {
        return tagOffset;
    }

    public void setTagOffset(int tagOffset)
    {
        this.tagOffset = tagOffset;
    }

    public int getLengthOffset()
    {
        return lengthOffset;
    }

    public void setLengthOffset(int lengthOffset)
    {
        this.lengthOffset = lengthOffset;
    }

    public int getValueOffset()
    {
        return valueOffset;
    }

    public void setValueOffset(int valueOffset)
    {
        this.valueOffset = valueOffset;
    }

    public byte[] getValueRef()
    {
        return valueRef;
    }

    public void setValueRef(byte[] valueRef)
    {
        this.valueRef = valueRef;
    }

    public byte[] getValue()
    {
        return value;
    }

    public void setValue(byte[] value)
    {
        this.value = value;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }


    public byte getTag()
    {
        if (this.valueRef == null)
            return this.tag;
        else
            return valueRef[tagOffset];
    }

}
