package demo.asn1viewer.util;

import java.math.*;
import java.util.*;
import java.io.*;

import org.oregan.util.asn1.*;
import org.oregan.util.*;
import demo.asn1viewer.ui.*;
import demo.asn1viewer.ui.internalframes.*;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 15-Jul-2003
 * Time: 16:17:31
 * To change this template use Options | File Templates.
 */
public class TagToString
{
    private static boolean intAsHex = true;

    public static String toDisplayableTag(TLV rootnode, boolean expandValue)
    {
        byte tag = rootnode.getValueRef()[rootnode.getTagOffset()];
        switch (tag)
        {
            case 0x01:  // Boolean
                String boolStr = "Boolean";
                if (rootnode.getValueRef()[rootnode.getValueOffset()] == 0x00)
                    boolStr = boolStr + " " + false;
                else
                    boolStr = boolStr + " " + true;
                return boolStr;
            case 0x02:  // Integer
                String integerStr = "Integer";
                byte[] intVal = new byte[rootnode.getLength()];
                System.arraycopy(rootnode.getValueRef(), rootnode.getValueOffset(), intVal, 0, intVal.length);
                BigInteger bI = new BigInteger(intVal);
                if (bI.compareTo(BigInteger.valueOf(Settings.getLargestDisplayableASN1IntegerValue())) <= 0)
                {
                    integerStr += " " + bI.toString((intAsHex ? 16 : 10));
                }
                return integerStr;
            case 0x23:	// Constructed BitString
            case 0x03:  // Bit String
                String s = "Bit String";
                if (expandValue)
                {
                    StringBuffer buff = new StringBuffer();
                    byte[] bitVal = new byte[rootnode.getLength() + rootnode.getLengthOfLength() + 1];
                    System.arraycopy(rootnode.getValueRef(), rootnode.getTagOffset(), bitVal, 0, bitVal.length);
                    int lines = (bitVal.length / 16) + 1;
                    for (int i = 0; i < lines; i++)
                    {
                        int len = 16;
                        if (i == lines - 1)
                            len = bitVal.length % 16;
                        //                        System.out.println("len = " + len);
                        //                        System.out.println("bitVal.length = " + bitVal.length);
                        buff.append(Util.toHexString(bitVal, i * 16, len, true));
                        buff.append("\r\n");
                    }
                    s += "\r\n" + buff.toString();
                }
                return s;
            case 0x24:	// Constructed Octet String
            case 0x04:  // Octet String
                String octStr = "Octet String";
                if (expandValue)
                {
                    StringBuffer buff = new StringBuffer();
                    byte[] octVal = new byte[rootnode.getLength() + rootnode.getLengthOfLength() + 1];
                    System.arraycopy(rootnode.getValueRef(), rootnode.getTagOffset(), octVal, 0, octVal.length);
                    int lines = (octVal.length / 16) + 1;
                    for (int i = 0; i < lines; i++)
                    {
                        int len = 16;
                        if (i == lines - 1)
                            len = octVal.length % 16;
                        //                        System.out.println("len = " + len);
                        //                        System.out.println("octVal.length = " + octVal.length);
                        buff.append(Util.toHexString(octVal, i * 16, len, true));
                        buff.append("\r\n");
                    }
                    octStr += "\r\n" + buff.toString();
                }
                return octStr;

            case 0x05:  // Null
                return "NULL";

            case 0x06:  // Object Identifier
                String oidStr = "Object Identifier";
                try
                {

                    byte[] oidVal = new byte[rootnode.getLength() + rootnode.getLengthOfLength() + 1];
                    System.arraycopy(rootnode.getValueRef(), rootnode.getTagOffset(), oidVal, 0, oidVal.length);
                    return oidStr + " "
                            + DERViewer.oidmapper.getDesc(Util.toHexString(oidVal, 0, oidVal.length, true));
                } catch (Exception e)
                {

                    byte[] oidVal = new byte[rootnode.getLength()];

                    System.arraycopy(rootnode.getValueRef(), rootnode.getValueOffset(), oidVal, 0, oidVal.length);
                    int[] oidValue = new int[oidVal.length + 1];         //will probably be shorter
                    oidValue[1] = Util.toInt(oidVal[0]) % 40;
                    oidValue[0] = (Util.toInt(oidVal[0]) - oidValue[1]) / 40;

                    int value = 0;
                    int oidIndex = 2;

                    for (int i = 1; i < oidVal.length; i++)
                    {
                        value = (value << 7) + (oidVal[i] & 0x7f);
                        if ((oidVal[i] & 0x80) != 0x80)
                        {
                            oidValue[oidIndex++] = value;
                            value = 0;
                        }
                    }

                    String dot = new String(".");
                    StringBuffer buf = new StringBuffer("");

                    for (int i = 0; i < oidIndex - 1; i++)
                        buf.append(Integer.toString(oidValue[i])).append(dot);

                    buf.append(Integer.toString(oidValue[oidIndex - 1]));
                    return oidStr + " " + buf.toString();

                }

            case 0x0a:  // Enumerated
                return "Enumerated";

            case 0x12:  // Numeric String
                return "Numeric String" + "\"" + tlvToString(rootnode) + "\"";
            case 0x13:  // Printable String
                return "Printable String " + "\"" + tlvToString(rootnode) + "\"";
            case 0x14:  // T61 String

                return "T61 String / Teletext String " + "\"" + tlvToString(rootnode) + "\"";
            case 0x16:  // IA5 String
                return "IA5 String " + "\"" + tlvToString(rootnode) + "\"";
            case 0x1a:  // Visible String
                return "Visible String " + "\"" + tlvToString(rootnode) + "\"";
            case 0x0c:
                return "UTF-8 String " + "\"" + tlvToString(rootnode) + "\"";
            case 0x1c:  // Universal String
                return "Universal String " + "\"" + tlvToString(rootnode) + "\"";
            case 0x1e:  // BMP String
                return "BMP String " + "\"" + tlvToString(rootnode) + "\"";
            case 0x17:  // UTC Time
                String timeStr = "UTC Time";
                try
                {
                    byte[] timeVal = new byte[rootnode.getLength()];
                    System.arraycopy(rootnode.getValueRef(), rootnode.getValueOffset(), timeVal, 0, timeVal.length);
                    String value = new String(timeVal, "ASCII");

                    Calendar cal = null;
                    TimeZone tz = TimeZone.getTimeZone("GMT");
                    int year, month, day, hour, minute, second = 0;
                    int tzhour, tzminute, timezoneoffsetThere = 0;

                    year = new Integer(value.substring(0, 2)).intValue();
                    // our complete year 2000 fix? That will be 10 grand please!!
                    year = ((year >= 50) ? 1900 + year : 2000 + year);
                    month = new Integer(value.substring(2, 4)).intValue() - 1;
                    day = new Integer(value.substring(4, 6)).intValue();
                    hour = new Integer(value.substring(6, 8)).intValue();
                    minute = new Integer(value.substring(8, 10)).intValue();

                    switch (value.length())
                    {
                        case 11:  //YYMMDDhhmmZ
                            cal = new GregorianCalendar(year, month, day, hour, minute);
                            break;
                        case 13:  //YYMMDDhhmmssZ
                            second = new Integer(value.substring(10, 12)).intValue();
                            cal = new GregorianCalendar(year, month, day, hour, minute, second);
                            break;
                            // hopefully these will never be used
                        case 15:  //YYMMDDhhmm+hh'mm'
                            cal = new GregorianCalendar(year, month, day, hour, minute);
                            tzhour = new Integer(value.substring(11, 13)).intValue();
                            tzminute = new Integer(value.substring(13, 15)).intValue();
                            timezoneoffsetThere = tzhour * 60 + tzminute;
                            if (value.substring(10, 11).equals("-"))
                                timezoneoffsetThere = (-1) * timezoneoffsetThere;
                            tz.setRawOffset(timezoneoffsetThere);
                            break;
                        case 17:  //YYMMDDhhmmss+hh'mm'
                            second = new Integer(value.substring(10, 12)).intValue();
                            cal = new GregorianCalendar(year, month, day, hour, minute, second);
                            tzhour = new Integer(value.substring(13, 15)).intValue();
                            tzminute = new Integer(value.substring(15, 17)).intValue();
                            timezoneoffsetThere = tzhour * 60 + tzminute;
                            if (value.substring(12, 13).equals("-"))
                                timezoneoffsetThere = (-1) * timezoneoffsetThere;
                            tz.setRawOffset(timezoneoffsetThere);
                            break;
                    }
                    cal.setTimeZone(tz);
                    timeStr += " " + cal.getTime().toString();

                } catch (UnsupportedEncodingException e)
                {
//                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                } catch (NumberFormatException e)
                {
//                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                }

                return timeStr;
            case 0x18:  // Generalized Time
                String genTimeStr = "Generalized Time";

                Calendar cal = null;
                try
                {
                    byte[] timeVal = new byte[rootnode.getLength()];
                    System.arraycopy(rootnode.getValueRef(), rootnode.getValueOffset(), timeVal, 0, timeVal.length);
                    String value = new String(timeVal, "ASCII");
                    cal = null;
                    TimeZone tz = TimeZone.getTimeZone("GMT");
                    int year, month, day, hour, minute, second = 0;
                    int tzhour, tzminute, timezoneoffsetThere = 0;

                    // if the value is null - return null.
                    if (value == null)
                    {
                        return null;
                    }

                    year = new Integer(value.substring(0, 4)).intValue();
                    month = new Integer(value.substring(4, 6)).intValue() - 1;
                    day = new Integer(value.substring(6, 8)).intValue();
                    hour = new Integer(value.substring(8, 10)).intValue();
                    minute = new Integer(value.substring(10, 12)).intValue();

                    int milliSecs = 0;
                    int fractionBegin = value.indexOf('.');
                    if (fractionBegin >= 0)
                    { // dot means fractional seconds
                        // extract the fractional part from string then process as normal, fracional is terminated by 'Z', '-', '+'
                        int fractionEnd = value.indexOf('Z', fractionBegin);
                        if (fractionEnd == -1)
                            fractionEnd = value.indexOf('+', fractionBegin);
                        if (fractionEnd == -1)
                            fractionEnd = value.indexOf('-', fractionBegin);
                        if (fractionEnd == -1)
                            ; // throw Exception

                        String fractionStr = value.substring(fractionBegin + 1, fractionEnd);
                        milliSecs = Integer.parseInt(fractionStr);
                        value = value.substring(0, fractionBegin) + value.substring(fractionEnd, value.length());
                    }


                    switch (value.length())
                    {
                        case 13:  //YYYYMMDDhhmmZ
                            cal = new GregorianCalendar(year, month, day, hour, minute);
                            break;
                        case 15:  //YYYYMMDDhhmmssZ
                            second = new Integer(value.substring(12, 14)).intValue();
                            cal = new GregorianCalendar(year, month, day, hour, minute, second);
                            break;
                        case 17:  //YYYYMMDDhhmm+hh'mm'
                            cal = new GregorianCalendar(year, month, day, hour, minute);
                            tzhour = new Integer(value.substring(13, 15)).intValue();
                            tzminute = new Integer(value.substring(15, 17)).intValue();
                            timezoneoffsetThere = tzhour * 60 + tzminute;
                            if (value.substring(12, 13).equals("-"))
                                timezoneoffsetThere = (-1) * timezoneoffsetThere;
                            tz.setRawOffset(timezoneoffsetThere);
                            break;
                        case 19:  //YYYYMMDDhhmmss+hh'mm'
                            second = new Integer(value.substring(12, 14)).intValue();
                            cal = new GregorianCalendar(year, month, day, hour, minute, second);
                            tzhour = new Integer(value.substring(15, 17)).intValue();
                            tzminute = new Integer(value.substring(17, 19)).intValue();
                            timezoneoffsetThere = tzhour * 60 + tzminute;
                            if (value.substring(14, 15).equals("-"))
                                timezoneoffsetThere = (-1) * timezoneoffsetThere;
                            tz.setRawOffset(timezoneoffsetThere);
                            break;
                    }
                    if (milliSecs != 0)
                        cal.set(Calendar.MILLISECOND, milliSecs);
                    cal.setTimeZone(tz);
                    //todo: Time is displayed in local, not as represented in ASN.1
                    genTimeStr += " " + cal.getTime().toString();
                } catch (UnsupportedEncodingException e)
                {
//                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                } catch (NumberFormatException e)
                {
//                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                }
                return genTimeStr;
            case 0x30:  // SEQUENCE (or SEQUENCE OF)
                return "Sequence (of)";
            case 0x31:
                return "Set (of)";
            default:
                {
                    if ((tag & 0x80) == 0x80)
                    {
                        return ("Context Specific [" + (tag & 0x1f) + "]");
                    } else
                        return "0x" + Integer.toHexString(tag);
                }
        }

    }

    private static String tlvToString(TLV node)
    {
        boolean unicode = false;

        if (node.getValueRef()[node.getTagOffset()] == 0x1e)
            unicode = true;

        String name;

        if (unicode)
        {
            name = (Util.toUnicodeString(node.getValueRef(), node.getValueOffset(), node.getLength()));
        } else
        {
            name = (Util.toString(node.getValueRef(), node.getValueOffset(), node.getLength()));
        }
        return name;
    }
}
