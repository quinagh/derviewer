package demo.asn1viewer;

import java.awt.event.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;

import demo.asn1viewer.ui.*;
import demo.asn1viewer.ui.internalframes.*;

public class OldMain extends JFrame
{
    public OldMain(String[] args)
    {
        super("DerViewer");
//        try
//        {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//            SwingUtilities.updateComponentTreeUI(this);
//        } catch (Exception e)
//        {
//            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//        }

        final JDesktopPane jDesktopPane = new JDesktopPane();

        final ImageIcon ic = new ImageIcon(getClass().getResource("/flattenPackages.png"));
        setIconImage(ic.getImage());


        getContentPane().add(new JScrollPane(jDesktopPane));


        JMenu window = new JMenu("Window");

        final JMenuItem cascade = new JMenuItem("Cascade");
        window.add(cascade);

        cascade.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (e.getSource() == cascade)
                {
                    cascade(jDesktopPane);

                }
            }

        });


        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        JMenuItem open = new JMenuItem("Open");
        open.setMnemonic('O');
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
        open.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser opener = new JFileChooser(System.getProperty("user.dir"));
                opener.setMultiSelectionEnabled(true);
                int result = opener.showOpenDialog(jDesktopPane);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    File[] f = opener.getSelectedFiles();
                    for (int i = 0; i < f.length; i++)
                    {
                        File file = f[i];
                        try
                        {
                            DERViewer derViewer = new DERViewer(file);
                            derViewer.setFrameIcon(ic);
                            jDesktopPane.add(derViewer);
                        } catch (IOException e1)
                        {

                        }
                    }
                }
            }
        });
        JMenuItem save_as = new JMenuItem("Save As");
        save_as.setMnemonic('A');
        save_as.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        save_as.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
                if (selectedFrame == null)
                {
                    if (jDesktopPane.getAllFrames().length == 0)
                    {
                        //None to save...
                        //todo: add error message "No frame open".
                        return;
                    } else
                    {
                        selectedFrame = jDesktopPane.getAllFrames()[0];
                    }
                }
                if (selectedFrame != null)
                    ((DERViewer) selectedFrame).saveAs();
            }
        });

        JMenuItem save_selected = new JMenuItem("Save selected");
        save_selected.setMnemonic('s');
        save_selected.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
                if (selectedFrame == null)
                {
                    if (jDesktopPane.getAllFrames().length == 0)
                    {
                        //None to save...
                        //todo: add error message "No frame open".
                        return;
                    } else
                    {
                        selectedFrame = jDesktopPane.getAllFrames()[0];
                    }
                }
                if (selectedFrame != null)
                    ((DERViewer) selectedFrame).saveSelected();
            }
        });


        JMenuItem close = new JMenuItem("Close");
        close.setMnemonic('C');
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.CTRL_MASK));
        close.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
                if (selectedFrame == null)
                {
                    if (jDesktopPane.getAllFrames().length == 0)
                    {
                        //None to save...
                        //todo: add error message "No frame open".
                        return;
                    }
                }
                if (selectedFrame != null)
                    selectedFrame.doDefaultCloseAction();
            }
        });
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic('x');
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        file.add(open);
        file.add(save_as);
        file.add(save_selected);
        file.add(close);
        file.add(sep);
        file.add(exit);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');

        JMenuItem copyAsText = new JMenuItem("Copy as text");
        copyAsText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
        copyAsText.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
                if (selectedFrame == null)
                {
                    if (jDesktopPane.getAllFrames().length == 0)
                    {
                        //None to save...
                        //todo: add error message "No frame open".
                        return;
                    } else
                    {
                        selectedFrame = jDesktopPane.getAllFrames()[0];
                    }
                }
                if (selectedFrame != null)
                {
                    String toCopy = ((DERViewer) selectedFrame).selectedToString();
                    StringSelection ss = new StringSelection(toCopy);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
                }


            }
        });

        editMenu.add(copyAsText);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        menuBar.add(editMenu);
        menuBar.add(window);

        setJMenuBar(menuBar);


        for (int i = 0; i < args.length; i++)
        {
            try
            {
                DERViewer derViewer = new DERViewer(new File(args[i]));
                derViewer.setFrameIcon(ic);
                jDesktopPane.add(derViewer);
            } catch (IOException e)
            {

            }
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        validate();

        pack();
        setSize(1024, 768);

        cascade(jDesktopPane);
    }

    /**
     * Cascade the internal frames leaving the currently selected frame to the lower right and on top.
     * @param jDesktopPane
     */
    private void cascade(JDesktopPane jDesktopPane)
    {
        JInternalFrame[] frames = jDesktopPane.getAllFrames();
        int x = 0;
        int y = 0;

        JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
        for (int i = 0; i < frames.length; i++)
        {
            JInternalFrame frame = frames[i];
            if (frame != selectedFrame)
            {
                frame.reshape(x, y, frame.getWidth(), frame.getHeight());
                frame.toFront();
                x += 25;
                y += 25;
            }

        }
        if (selectedFrame != null)
        {
            selectedFrame.reshape(x, y, selectedFrame.getWidth(), selectedFrame.getHeight());
            selectedFrame.toFront();
        }


    }

    public static void main(String[] args)
    {
        new OldMain(args).setVisible(true);
    }
}
