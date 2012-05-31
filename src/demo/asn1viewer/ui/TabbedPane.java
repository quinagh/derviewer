package demo.asn1viewer.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Set;

public class TabbedPane extends JPanel
{
    JPanel tabs = new JPanel();
    CardLayout cardLayout = new CardLayout();
    JPanel panes = new JPanel(cardLayout);
    Hashtable name2buttons = new Hashtable();
    Hashtable name2panels = new Hashtable();

    public TabbedPane()
    {
        setLayout(new BorderLayout(0,0));
//        setOpaque(false);
        tabs.setBorder(new ULineBorder(Color.gray,"n"));
        tabs.setLayout(new FlowLayout(FlowLayout.LEADING,1,0));
        add(panes, BorderLayout.CENTER);
        add(tabs, BorderLayout.SOUTH);
    }

    public void addTab(String name, JComponent panel)
    {
        cardLayout.addLayoutComponent(panel, name);
        panes.add(name, panel);
        cardLayout.show(panes, name);
        TabButton button = new TabButton(name);
        button.setActionCommand(name);

        name2buttons.put(name,button);
        name2panels.put(name,panel);

        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setSelected(e.getActionCommand());
            }
        });

      /*  button.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e)
            {

                if(e.getButton()==MouseEvent.BUTTON3)
                {
                    JPopupMenu menu = getMenu((((JButton) e.getSource())).getText());
                    System.out.println("menu = " + menu);

                    menu.setVisible(true);

                }
            }
        });
*/
        tabs.add(button);
        setSelected(name);
    }

    public void removeTab(String name)
    {
        JComponent comp = (JComponent) name2panels.get(name);
        cardLayout.removeLayoutComponent(comp);
        JButton tab = (JButton) name2buttons.get(name);
        tabs.remove(tab);
        name2buttons.remove(name);
        name2panels.remove(name);
        tab = null;
        comp = null;
        Set keys = name2buttons.keySet();
        if(keys.isEmpty())
        {
            //todo: implement an MRU scheme or at least pick a neighbour.
        }
        else
        {
            setSelected((String) keys.iterator().next());
        }
        tabs.invalidate();
        panes.invalidate();
        this.invalidate();
    }

/*
    private JPopupMenu getMenu(String name)
    {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem closeButton = new JMenuItem("Close");
        closeButton.setActionCommand(name);
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                removeTab(e.getActionCommand());
            }
        });
        popupMenu.add(closeButton);
        popupMenu.pack();
        return popupMenu;
    }
*/

    public void setSelected(String name)
    {
        cardLayout.show(panes,name);
        Enumeration en = name2buttons.keys();
        while (en.hasMoreElements())
        {
            String s = (String) en.nextElement();
            TabButton b = (TabButton) name2buttons.get(s);
            if(s.compareTo(name)==0)
            {
                b.setSelected(true);
            }else
            {
                b.setSelected(false);
            }

        }

    }


    public static void main(String[] args) throws IOException
    {
        JFrame frame = new JFrame("Tabbed Pane");
        TabbedPane tabbedPane = new TabbedPane();


        for (int i = 0; i < args.length; i++)
        {
            String fname = args[i];
            File file = new File(fname);

            TreeAndHexPanel hexPanel = new TreeAndHexPanel(file);

            tabbedPane.addTab(file.getName(), hexPanel);
        }

        frame.getContentPane().add(tabbedPane);


        frame.pack();

        frame.setVisible(true);

    }

}
