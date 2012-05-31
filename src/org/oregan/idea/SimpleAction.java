package org.oregan.idea;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.*;

/**
 * Created by IntelliJ IDEA.
 * User: koregan
 * Date: 14-Jul-2003
 * Time: 18:30:19
 * To change this template use Options | File Templates.
 */
public class SimpleAction extends AnAction
{
    public void actionPerformed(AnActionEvent anActionEvent)
    {
        VirtualFile file = (VirtualFile) anActionEvent.getDataContext().getData("virtualFile");
        Project project = ProjectManager.getInstance().getOpenProjects()[0];

        Class[] components = project.getComponentInterfaces();
        ASN1Viewer viewer = (ASN1Viewer) project.getComponent(ASN1Viewer.class);

        viewer.loadFile(file.getPath());

//        Logger l = Logger.getInstance("simple");
//        for (int i = 0; i < components.length; i++)
//        {
//            Class component = components[i];
//            l.info(component.getName());
//        }
//
//        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
//
//        try
//        {
//
//
//
//            toolWindowManager.unregisterToolWindow(ASN1Viewer.TOOL_WINDOW_ID);
//        } catch (Exception e)
//        {
//            System.out.println("Not REG'd");
//        }
//        TreeAndHexPanel dv = null;
//
//        try
//        {
//            dv = new TreeAndHexPanel(new File(file.getPath()));
//            dv.setBorder(BorderFactory.createTitledBorder(
//                        BorderFactory.createEtchedBorder(),
//                        "File - " + file.getName(), TitledBorder.LEADING,
//                        TitledBorder.TOP));
//
//        } catch (IOException e)
//        {
//            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//        }
//        ToolWindow tw = toolWindowManager.registerToolWindow(ASN1Viewer.TOOL_WINDOW_ID, dv, ToolWindowAnchor.BOTTOM);
//        tw.setTitle("ASN1.Viewer");
//        tw.show(new Runnable(){
//            public void run()
//            {
//
//            }
//        });
    }
}
