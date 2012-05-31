package org.oregan.idea;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import demo.asn1viewer.ui.TreeAndHexPanel;
import demo.asn1viewer.ui.TabbedTreeAndHexPane;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.awt.*;

public class ASN1Viewer implements ProjectComponent
{
    private Project myProject;

    private TabbedTreeAndHexPane dv;

    public static final String TOOL_WINDOW_ID = "ASN1Viewer";
    private ToolWindow myToolWindow;

    public ASN1Viewer(Project project)
    {
        myProject = project;
    }

    public void projectOpened()
    {
        initToolWindow();
    }

    public void projectClosed()
    {
        unregisterToolWindow();
    }

    public void initComponent()
    {
        // empty
    }

    public void disposeComponent()
    {
        // empty
    }

    public String getComponentName()
    {
        return "SimpleToolWindow.ASN1Viewer";
    }

    private void initToolWindow()
    {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
        dv = new TabbedTreeAndHexPane();
        myToolWindow = toolWindowManager.registerToolWindow(TOOL_WINDOW_ID, dv, ToolWindowAnchor.BOTTOM);

    }

    public void loadFile(String fname)
    {
        dv.addFile(new File(fname));
        myToolWindow.activate(new Runnable()
        {
            public void run()
            {
                dv.repaint(100);
            }
        });

    }

    private void unregisterToolWindow()
    {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
        toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
    }
}
