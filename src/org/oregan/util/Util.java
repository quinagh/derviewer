package org.oregan.util;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 07-Jul-2003
 * Time: 18:19:51
 * To change this template use Options | File Templates.
 */
public class Util
{
    public static boolean cmpByteArrays(byte[] byte1, byte[] byte2)
    {
        boolean result = false;


        // if either array is null, they are not equal
        if (byte1 == null || byte2 == null)
        {
            return result;
        }

        // if both arrays are zero in length, they are equal
        // if only one array is zero in length, they are not equal

        int i = 0;
        if (byte1.length == byte2.length)
        {
            result = true;
            while ((i < byte1.length) & result)
            {
                result = ((byte1[i] == byte2[i]));
                i++;
            }
        }

        return result;
    }

    public static boolean cmpByteArrays(byte[] lhsBytes, int lhsOffset, byte[] rhsBytes, int rhsOffset, int length)
    {
        if (lhsBytes.length < lhsOffset + length)
            return false;
        if (rhsBytes.length < rhsOffset + length)
            return false;

        for (int i = 0; i < length; i++)
        {
            if (rhsBytes[i + rhsOffset] != lhsBytes[i + lhsOffset])
                return false;
        }
        return true;
    }

    /**
     * Converts a long to a network ordered byte array (MSByte first).
     * Array will contain no leading zeros, and thus may be between 1 and 8 bytes long.
     *
     * @param   l long...
     */
    public static byte[] toBytes(long l)
    {
        byte[] bytes = new byte[8];

        for (int i = 7; i >= 0; i--)
        {
            bytes[i] = (byte) (l & 0xff);
            l >>>= 8;
        }
        // get rid of any leading zero bytes
        int index = 7;
        boolean found = false;
        for (int i = 0; ((i < 7) && (found == false)); i++)
        {
            if (bytes[i] != 0)
            {
                index = i;
                found = true;
            }
        }

        byte[] realBytes = new byte[8 - index];
        System.arraycopy(bytes, index, realBytes, 0, realBytes.length);
        return realBytes;
    }

    /**
     * Hex encode some binary data.
     *
     * @param            data The data to encode.
     * @param            offset The offset from which to start encoding.
     * @param            length The length to encode.
     * @return           The hex encoded data.
     */
    public static String toHexString(byte[] data, int offset, int length)
    {
        // prevents NullPointerException, ArrayOutOfBoundsException
        // and NegativeIndexException.
        if (data == null || data.length == 0 || offset >= data.length ||
                length > data.length || offset + length > data.length ||
                offset < 0 || length < 0)
        {
            return new String();
        }
        return toHexString(data, offset, length, false);
    }

    public static String toHexString(byte[] data, int offset, int length, boolean pad)
    {
        //todo padding
        if (length == 0)
            return "";

        if (!pad)
        {
            int len = length * 2;
            char[] chars = new char[len];
            for (int i = 0; i < length; ++i)
            {
                chars[i * 2] = chex[(data[offset + i] >> 4) & 0xf];
                chars[i * 2 + 1] = chex[data[offset + i] & 0xf];
            }
            return new String(chars);
        } else
        {
//            System.out.println("length = " + length);
            int len = length * 2 + (length - 1);

            // 10 * 2 + (len
            char[] chars = new char[len];
            int charPtr = 0;

            for (int i = 0; i < length; ++i)
            {
//                System.out.println("i = " + i);
//                System.out.println("charPtr = " + charPtr);
//                System.out.println("chars = " + new String(chars));
                chars[charPtr] = chex[(data[offset + i] >> 4) & 0xf];
                chars[charPtr + 1] = chex[data[offset + i] & 0xf];
                if (i != (length - 1))
                    chars[charPtr + 2] = ' ';
                charPtr += 3;
            }
            return new String(chars);
        }
    }

    /**
     * Converts a byte array to a string. Assumes that the byte array is made up of
     * the low bytes from a string; that is, if it is the Unicode encoding of a string
     * then use the getUnicodeString method. This method is preferred to using the
     * string constructor, which takes a byte array since it is independent of the
     * default encoding system.
     *
     * @param            bytes The byte array from which to dissect.
     * @param            offset The byte subarray offset.
     * @param            length The byte subarray length.
     * @return           A string.
     */
    public static String toString(byte[] bytes, int offset, int length)
    {
        // check to ensure that the byte array is not null
        // if it is null return empty string.
        if (bytes == null)
            return new String();

        try
        {
            char[] chars = new char[length];
            for (int i = 0; i < length; ++i)
            {
                chars[i] = (char) bytes[offset + i];
            }
            return new String(chars);
        } catch (ArrayIndexOutOfBoundsException ex)
        {
            return new String();
        } catch (NegativeArraySizeException ex)
        {
            return new String();
        }

    }

    /**
     * Converts a byte array a string. Assumes that the byte array is made up of
     * Unicode byte encoding from a string; that is, each pair of bytes make up a
     * character. We assume that the bytes are in big-endian format.
     * <p>
     * For example, the string "Pavement"  has ASCII encoding - 50 61 76 65 6D 65 6E
     * 74. The Unicode encoding (big endian) of this is 00 50 00 61 00 76 00 65 00 6D
     * 00 65 00 6E 00 74 (the most significant byte (MSB) is first in each pair
     * encoding, in this example, the ASCII byte is the least significant byte (LSB)).
     * Different operating systems use different endianess - most Unix system uses big
     * endianess while Microsoft uses little endianess. BMPString uses Unicode
     * encoding (without marking it) of strings. It requires that the bytes be ordered
     * in network byte order (that is, big endian). As a result all of the Unicode
     * related methods in J/CRYPTO encode strings (and expect string encodings) to be
     * big endian.
     *
     * @param            bytes The byte array from which to dissect.
     * @param            offset The byte subarray offset.
     * @param            length The byte subarray length.
     * @return           A string.
     */
    public static String toUnicodeString(byte[] bytes, int offset, int length)
    {
        // if byte array null return empty string
        if (bytes == null || length > bytes.length)
        {
            return new String();
        }

        char ac[] = new char[length / 2];
        for (int i = 0; i != ac.length; i++)
        {
            byte x = bytes[offset + (2 * i)];
            byte y = bytes[offset + (2 * i + 1)];
            ac[i] = (char) (x << 8 | y & 0xff);
        }

        return new String(ac);

    }

    public static int toInt(byte b)
    {
        if (b < 0)
            return ((int) (b & 0x7F)) + 128;
        return (int) b;
    }

    public static final String hex = "0123456789abcdef";
    public static final char[] chex = hex.toCharArray();


}
