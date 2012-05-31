package demo.asn1viewer.ui;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 14-Jul-2003
 * Time: 13:07:47
 * To change this template use Options | File Templates.
 */
public class Settings
{
    private static int numLineScroll = 1;
    private static long maxValue = Long.MAX_VALUE;
    private static boolean autoScrollHexFromTree = true;

    public static boolean isAutoScrollHexFromTree()
    {
        return autoScrollHexFromTree;
    }

    public static void setAutoScrollHexFromTree(boolean autoScrollHexFromTree)
    {
        Settings.autoScrollHexFromTree = autoScrollHexFromTree;
    }

    public static int getNumLineScroll()
    {
        return numLineScroll;
    }

    public static void setNumLineScroll(int numLineScroll)
    {
        Settings.numLineScroll = numLineScroll;
    }

    public static long getLargestDisplayableASN1IntegerValue()
    {
        return maxValue;
    }

}
