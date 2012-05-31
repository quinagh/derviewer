package org.oregan.util.asn1;


/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 20-Jun-2003
 * Time: 09:14:58
 * To change this template use Options | File Templates.
 */
public class ASN1
{
    public static final byte INTEGER = 0x02;
    public static final byte SEQUENCE = 0x30;
    public static final byte OBJECTIDENTIFIER = 0x06;
    public static final byte OCTETSTRING = 0x04;
    public static final byte BITSTRING = 0x03;
    public static final byte UTF8STRING = 0x0c;

    public static final byte[] OIDsSHA1 = new byte[]{(byte) 0x2B, (byte) 0x0E, (byte) 0x03, (byte) 0x02, (byte) 0x1A};//   - SHA-1
    public static final byte[] OIDsSHA1withRSAEncryption = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x01, (byte) 0x05};//   - SHA-1

    public static final byte[] OIDsData = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x07, (byte) 0x01};
    public static final byte[] OIDsEncryptedData = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x07, (byte) 0x06};

    public static final byte[] OIDsPKCS12KeyBag = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x0C, (byte) 0x0A, (byte) 0x01, (byte) 0x01};
    public static final byte[] OIDsPKCS12P8ShroudedKeyBag = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x0C, (byte) 0x0A, (byte) 0x01, (byte) 0x02};
    public static final byte[] OIDsPKCS12CertBag = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x0C, (byte) 0x0A, (byte) 0x01, (byte) 0x03};
    public static final byte[] OIDsPKCS12X509Certificate = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x09, (byte) 0x16, (byte) 0x01};
    public static final byte[] OIDsPKCS12FriendlyName = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x09, (byte) 0x14};
    public static final byte[] OIDsPKCS12LocalKeyID = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x09, (byte) 0x15};

    public static final byte[] SHA1DigestInfoTemplate = new byte[]{(byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x9, (byte) 0x6, (byte) 0x5, (byte) 0x2b, (byte) 0xe, (byte) 0x3, (byte) 0x2, (byte) 0x1a, (byte) 0x5, (byte) 0x0, (byte) 0x4, (byte) 0x14};

    public static final byte[] OIDs_pbeWithSHA13keyTripleDESCBC = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x0C, (byte) 0x01, (byte) 0x03};
    public static final byte[] OIDs_pbeWithSHAAnd40BitRC2CBC = new byte[]{(byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x0C, (byte) 0x01, (byte) 0x06};

    public static final byte[] OIDsBasicConstraints = new byte[]{(byte) 0x55, (byte) 0x1D, (byte) 0x13};
    public static final byte[] OIDsKeyUsage = new byte[]{(byte) 0x55, (byte) 0x1D, (byte) 0x0F};



    /**
     * Calculate the number of components in this component.
     * @param pfxBytes der to inspect
     * @param offset should be positioned at the tag of the 1st component.
     * @param length length of current component.
     * @return number of components
     */
    public static int numComponents(byte[] pfxBytes, int offset, int length)
    {
        int count = 0;
        int i = offset + 1; //skip tag.
        int maxIndex = length + offset;
        while (i < maxIndex)
        {
            i = jumpOverComponent(pfxBytes, i);
            i++; //skip next tag
            count++; // increment.
        }
        return count;
    }

    /**
     * Calculate the offset needed to skip over the next component.
     * @param pfxBytes der to inspect.
     * @param offset should be after the tag of the component to skip.
     * @return new offset positioned at the next tag after the current component.
     */
    public static int jumpOverComponent(byte[] pfxBytes, int offset)
    {
        int lol = getLengthOfLength(pfxBytes, offset);
        int len = getLength(pfxBytes, offset);
        int skip = lol + len;
        //        System.out.println("skipping "+ skip);
        return offset + skip;
    }

    /**
     * Skip over the length data to the start of the value.
     * @param pfxBytes der to inspect.
     * @param offset should be after the tag of the component.
     * @return new offset positioned at the start of the value.
     */
    public static int jumpIntoComponent(byte[] pfxBytes, int offset)
    {
        int lol = getLengthOfLength(pfxBytes, offset);
        return offset + lol;
    }

    /**
     * Calculate the length of the value.
     * @param asn1 der to inspect
     * @param offset should be after the tag of the component.
     * @return length of value
     */
    public static int getLength(byte[] asn1, int offset)
    {
        if (offset >= asn1.length)
            return -1;
        int length = asn1[offset] & 0xff;
        if (length < 128)
        {
            return length;
        } else if (offset + length - 128 >= asn1.length)
        {
            return -1;
        } else
        {
            int value = 0;
            for (int i = 128; i < length; ++i)
                value = (value << 8) + (asn1[offset + 1 + i - 128] & 0xff);
            return value;
        }
    }

    /**
     * Calculate the number of bytes occupied by the length value.
     * @param asn1 der to inspect.
     * @param offset should be after the tag of the component.
     * @return length of the length value.
     */
    public static int getLengthOfLength(byte[] asn1, int offset)
    {
        if (offset >= asn1.length)
            return -1;
        int length = asn1[offset] & 0xff;
        if (length < 128)
        {
            return 1;
        } else
        {
            return 1 + length - 128;
        }
    }

    /**
     * returns an array of a DER encoded ASN1Intger length .
     * have this extra method to see if the first byte is negative > 128 if so prepend
     * 0x00
     */
    public static byte[] encodeLength(int length)
    {
        byte[] returnBytes;
        int myLength = length;

        if (myLength < 128)
        {
            returnBytes = new byte[1];
            returnBytes[0] = (byte) myLength;
        } else
        {
            byte[] tmp = new byte[4];
            int len = 0;

            for (; myLength > 0; len++)
            {
                tmp[3 - len] = (byte) (myLength & 0xff);
                myLength = myLength >>> 8;
            }

            returnBytes = new byte[len + 1];      // extra byte for length of length (of course we are leaving out the case when the length of the length is greater than one byte boo hoo)
            byte b = (byte) (0x80 | len);
            returnBytes[0] = b;
            System.arraycopy(tmp, 4 - len, returnBytes, 1, len);
        }

        return returnBytes;
    }

    /* public static byte[] encodeLength(mpa_num integer) throws MPAException
     {
         byte[] asnContents = null;
         asnContents = integer.mpa_getbytearray(asnContents);

         if ((asnContents[0] & 0x80) == 0x80)
         {
             byte[] tmp = new byte[asnContents.length + 1];
             System.arraycopy(asnContents, 0, tmp, 1, asnContents.length);
             tmp[0] = (byte) 0x00;
             asnContents = tmp;
         }

         return encodeLength(asnContents.length);

     }*/

    public static boolean isOID(byte[] pfxBytes, int offset, int len, byte[] compareTo)
    {
        //                System.out.println("len = " + len);
        //                System.out.println("compareTo = " + compareTo.length);
        if (len != compareTo.length)
            return false;
        for (int i = 0; i < len; i++)
        {
            byte X = pfxBytes[i + offset];
            byte Y = compareTo[i];
            //                        System.out.println(Integer.toHexString(X)+ " = " + Integer.toHexString(Y));
            if (X != Y)
                return false;
        }
        return true;
    }

    public static boolean[] decodeBitString(TLV bitString)
    {
        int numInsigBits = bitString.getValueRef()[bitString.getValueOffset()];

        byte[] bytes = new byte[bitString.getLength() - 1];
        System.arraycopy(bitString.getValueRef(), bitString.getValueOffset() + 1, bytes, 0, bytes.length);
        return toBooleans(bytes, numInsigBits);
    }


    public static boolean[] toBooleans(byte[] bytes, int numUnusedBits)
    {
        if (numUnusedBits < 0 || numUnusedBits > 7)
        {
            throw new java.lang.IllegalArgumentException("Utils::BytesToBooleans - number " +
                    "of unused bits in last byte must me in the range 0-7, " +
                    "we have " + numUnusedBits);
        }
        if (bytes == null)
        {
            return new boolean[0];
        }

        boolean[] bools = new boolean[(bytes.length << 3) - numUnusedBits];

        int size = bools.length;
        int count = 0;

        for (int i = 0; i < size; i++)
        {
            bools[i] = (bytes[count] & (0x1 << (7 - (i % 8)))) == 0 ? false : true;

            if (i % 8 == 7)
                count++;
        }

        return bools;
    }

}
