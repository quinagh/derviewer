package org.oregan.util;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 07-Jul-2003
 * Time: 18:35:02
 * To change this template use Options | File Templates.
 */
public class UtilTest
{
    public static void main(String[] args) throws IOException
    {


//        for(int i=0;i<Integer.MAX_VALUE;i+=16)
//        {
//
//            byte[] x = Util.toBytes((long)i);
//            if(i!=0)
//                System.out.print(",");
//            System.out.print(i +"-->"+Util.toHexString(x,0,x.length));
//            if(i%10==0)
//                System.out.println("");
//        }
//


//        TLV tlv = new TLV();
//        byte[] x = new byte[9999];
//        tlv.setValue(x);
//
//        FileOutputStream fos= new FileOutputStream("oct.ber");
//        fos.write(ASN1.OCTETSTRING);
//        byte[] len = ASN1.encodeLength(x.length);
//        fos.write(len);
//        fos.write(x);
//
//        fos.close();


        byte eighty = (byte) 0x80;  // Context specific
        byte twenty = (byte) 0x20;  // structured.
        byte oneF = (byte) 0x1F;

        byte azero = (byte) 0xA0;
        byte athree = (byte) 0xA3;

        byte and = (byte) ((eighty | twenty) & oneF);
        System.out.println("Integer.toString(and,2) = " + Integer.toString(and, 2));

        and = (byte) (athree & oneF);

        System.out.println("Integer.toString(and,2) = " + Integer.toString(and, 2));


        System.out.println("--");


    }
}
