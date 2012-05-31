package demo.asn1viewer.ui;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 12-Jul-2003
 * Time: 23:03:28
 * To change this template use Options | File Templates.
 */
public class OIDMapper
{
    private Hashtable map = new Hashtable(500);
    public final String OID_PREFIX = "OID = ";
    public final String DESC_PREFIX = "Description = ";

    public OIDMapper(File cfg) throws IOException
    {
        FileReader fr = new FileReader(cfg);
        BufferedReader br = new BufferedReader(fr, 10240);
        String line;

        String oid = "";
        String desc = "";
        while ((line = br.readLine()) != null)
        {
            if (line.startsWith(OID_PREFIX))
            {
                addMapping(oid, desc);
                oid = line.substring(OID_PREFIX.length()).trim();
            }
            if (line.startsWith(DESC_PREFIX))
            {
                desc = line.substring(DESC_PREFIX.length()).trim();
            }
        }
        addMapping(oid, desc);
    }

    private void addMapping(String oid, String desc)
    {
//        System.out.println(oid + " = " + desc + " -- " + oid.hashCode());
        map.put(oid, desc);
    }

    public String getDesc(String oid)
    {
        String upperCase = oid.toUpperCase();
        return (String) map.get(upperCase);
    }


}
