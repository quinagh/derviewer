package demo.asn1viewer.ui.tree;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import demo.asn1viewer.ui.MyPrintingVisitor;
import demo.asn1viewer.ui.*;
import demo.asn1viewer.ui.text.*;
import demo.asn1viewer.util.*;
import demo.asn1viewer.layout.TableLayout;
import org.oregan.util.*;
import org.oregan.util.asn1.*;

public class ASN1TreePanel extends JPanel
{
    private DefaultTreeModel jTreeModel;
    private JTree jTree;

    private final ASN1TreeCellRenderer asn1TreeCellRenderer = new ASN1TreeCellRenderer();
    private JToggleButton autoScrollToggle;
    private JButton saveNode;
    private JLabel statusLabel;
    private JPopupMenu whatIfMenu;

    public ASN1TreePanel(TLV root, final TreeSelectionListener listener)
    {
        final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
        jTreeModel = new DefaultTreeModel(rootNode);

        jTree = new JTree(jTreeModel);
        jTree.setBorder(new EmptyBorder(1, 1, 1, 1));
        jTree.putClientProperty("JTree.lineStyle", "Angled");


        jTree.setCellRenderer(asn1TreeCellRenderer);
        jTree.addTreeSelectionListener(new TreeActionListener(listener));

        ImageIcon ic = new ImageIcon(getClass().getResource("/autoscrollToSource.png"));
        ImageIcon saveIC = new ImageIcon(getClass().getResource("/menu-saveall.png"));

        JPanel toolBar = new JPanel();
        toolBar.setBorder(new EmptyBorder(1, 1, 1, 1));
        int iconSize = 25;
        double[][] doubles = new double[][]{{iconSize},
                                            {iconSize, iconSize, TableLayout.FILL}};
        TableLayout tl = new TableLayout(doubles);



        //toolBar.setLayout(new GridLayout(1, 6));
        toolBar.setLayout(tl);


        autoScrollToggle = new JToggleButton(ic);
        autoScrollToggle.setBorderPainted(false);
        autoScrollToggle.setBorder(new LineBorder(Color.blue.darker(), 1, false));
        autoScrollToggle.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                if (autoScrollToggle.isEnabled())
                    autoScrollToggle.setBorderPainted(true);
            }

            public void mouseExited(MouseEvent e)
            {
                if (autoScrollToggle.isEnabled())
                    autoScrollToggle.setBorderPainted(false);
            }
        });


        autoScrollToggle.setActionCommand("autoScroll");
        autoScrollToggle.setToolTipText("Autoscroll in hex view");
        autoScrollToggle.setSelected(Settings.isAutoScrollHexFromTree());

        toolBar.add(autoScrollToggle, "0,0");

        autoScrollToggle.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ((HexView) listener).removeHighlights();
                //todo what if there is no selection.
                TreePath selectionPath = jTree.getSelectionPath();
                if (selectionPath == null)
                {
                    selectionPath = new TreePath(jTree.getModel().getRoot());
                }
                if (autoScrollToggle.isSelected())
                    ((HexView) listener).highlightTLV(((TLV) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject()));
            }
        });

        saveNode = new JButton(saveIC);
        saveNode.setActionCommand("save");
        saveNode.setToolTipText("Save selected component");
        saveNode.setBorderPainted(false);
        saveNode.setBorder(new LineBorder(Color.blue.darker(), 1, true));
        saveNode.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                if (saveNode.isEnabled())
                    saveNode.setBorderPainted(true);
            }

            public void mouseExited(MouseEvent e)
            {
                if (saveNode.isEnabled())
                    saveNode.setBorderPainted(false);
            }
        });

        toolBar.add(saveNode, "0,1");
        saveNode.addActionListener(new SaveActionListener());

        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.registerComponent(autoScrollToggle);
        toolTipManager.registerComponent(saveNode);

        setLayout(new BorderLayout(0, 0));

        add(toolBar, BorderLayout.WEST);
        toolTipManager.registerComponent(jTree);

        whatIfMenu = new JPopupMenu("What If");
        JMenuItem seqItem = new JMenuItem("Sequence of");
        whatIfMenu.add(seqItem);
        JMenuItem setItem = new JMenuItem("Set of");
        whatIfMenu.add(setItem);
        whatIfMenu.addSeparator();
        JMenuItem utf8Item = new JMenuItem("UTF-8 String");
        utf8Item.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                TreePath tp = jTree.getSelectionPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
                jTree.setSelectionPath(tp);
                TLV tlv = (TLV) node.getUserObject();
                byte tag = tlv.getValueRef()[tlv.getTagOffset()];
                //todo: continue from here ...
                //todo: do we copy the array or just modify as we go...
                byte[] tmpCopy = new byte[1+tlv.getLengthOfLength()+tlv.getLength()];
                System.arraycopy(tlv.getValueRef(),
                                 tlv.getLengthOffset(),
                                 tmpCopy,
                                 1,
                                 tmpCopy.length - 1);

                tmpCopy[0] = ASN1.UTF8STRING;

                System.out.println("tmpCopy = " + Util.toHexString(tmpCopy,0,tmpCopy.length,true));

                TLV changedTLV = DERParser.decode(tmpCopy);
                DefaultMutableTreeNode tmproot = new DefaultMutableTreeNode(changedTLV);
                DefaultTreeModel model = new DefaultTreeModel(tmproot);
                JTree tmpTree = new JTree(model);
                tmpTree.setCellRenderer(asn1TreeCellRenderer);
                buildTree(model,tmproot);
                expandAll(tmpTree,true);

                tmpTree.validate();

                JFrame win = new JFrame();
                win.setUndecorated(true);
                win.getContentPane().setLayout(new BorderLayout());
                win.getContentPane().add(tmpTree,BorderLayout.CENTER);
                win.pack();
                win.setLocationRelativeTo(jTree);
                tmpTree.addFocusListener(new FloatingWinFocusAdapter(win));
                win.addFocusListener(new FloatingWinFocusAdapter(win));
                win.setVisible(true);
                win.toFront();


            }
        });
        whatIfMenu.add(utf8Item);


        jTree.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    if (e.getSource() instanceof JTree)
                    {
                        JTree jTree = (JTree) e.getSource();
                        TreePath tp = jTree.getPathForLocation(e.getX(), e.getY());
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
                        jTree.setSelectionPath(tp);
                        TLV tlv = (TLV) node.getUserObject();
                        byte tag = tlv.getValueRef()[tlv.getTagOffset()];
                        if ((tag & 0x80) == 0x80)
                        {
                            whatIfMenu.show(jTree, e.getX(), e.getY());
                        }
                    }
                }
            }
        });



        JPanel treePanel = new JPanel(new BorderLayout(0, 0));
        treePanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JScrollPane scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroller.setViewportView(jTree);
        scroller.setViewportBorder(new EmptyBorder(0, 0, 0, 0));

        treePanel.add(scroller, BorderLayout.CENTER);
        this.statusLabel = new JLabel("© Kevin O'Regan 2003.");

        treePanel.add(statusLabel, BorderLayout.SOUTH);

        add(treePanel, BorderLayout.CENTER);
        Runnable r = new Runnable()
        {
            public void run()
            {
                traverse(rootNode);
                expandAll(jTree, true);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    private String prepDesc(TLV nodeToSave, JCheckBox encodingOnly)
    {
        String tagDesc = TagToString.toDisplayableTag(nodeToSave, false);

        int offset = (encodingOnly.isSelected() ? nodeToSave.getValueOffset() : nodeToSave.getTagOffset());

        int length = Math.min(20, (encodingOnly.isSelected() ?
                nodeToSave.getLength() :
                1 + nodeToSave.getLengthOfLength() + nodeToSave.getLength()));

        String hexView = Util.toHexString(nodeToSave.getValueRef(), offset, length, true);
        String desc = tagDesc + "\r\n\r\n" + hexView + "...";
        return desc;
    }

    private void traverse(DefaultMutableTreeNode root)
    {

        buildTree(this.jTreeModel,root);
    }

    private void buildTree(DefaultTreeModel model, DefaultMutableTreeNode root)
    {
        //        System.out.println("children.size() = " + children.size());
        Enumeration en = ((TLV) root.getUserObject()).enumeration();
        while (en.hasMoreElements())
        {
            TLV tlv = (TLV) en.nextElement();
            //            System.out.println("tlv = " + tlv);
            DefaultMutableTreeNode tlvNode = new DefaultMutableTreeNode(tlv);
            int childCount = root.getChildCount();
            //            System.out.println("childCount = " + childCount);
            model.insertNodeInto(tlvNode, root, childCount);
            traverse(tlvNode);
        }
    }

    public void expandAll(JTree tree, boolean expand)
    {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand)
    {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0)
        {
            for (Enumeration e = node.children(); e.hasMoreElements();)
            {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand)
        {
            tree.expandPath(parent);
        } else
        {
            tree.collapsePath(parent);
        }
    }

    public void doSaveAs()
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getModel().getRoot();
        doSave(node);

    }

    public void doSaveSelected()
    {
        TreePath selectionPath = jTree.getSelectionPath();
        if (selectionPath == null)
            selectionPath = new TreePath(jTree.getModel().getRoot());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
        doSave(node);
    }

    private void doSave(DefaultMutableTreeNode node)
    {
        final TLV nodeToSave = (TLV) node.getUserObject();

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "ASN.1 component to save", TitledBorder.CENTER, TitledBorder.TOP));
        final JTextArea area = new JTextArea();
        final JCheckBox saveValueOnly = new JCheckBox("Save value only/no encoding");
        saveValueOnly.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                area.setText(prepDesc(nodeToSave, saveValueOnly));
            }
        });
        panel.add(saveValueOnly, BorderLayout.SOUTH);

        area.setColumns(20);
        area.setEditable(false);
        area.setLineWrap(true);
        String desc = prepDesc(nodeToSave, saveValueOnly);
        area.setText(desc);
        panel.add(area, BorderLayout.CENTER);

        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setAccessory(panel);
        int retval = chooser.showSaveDialog(jTree);
        if (retval == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
                if (!saveValueOnly.isSelected())
                    fos.write(nodeToSave.getValueRef(), nodeToSave.getTagOffset(), 1 + nodeToSave.getLengthOfLength()
                            + nodeToSave.getLength());
                else
                    fos.write(nodeToSave.getValueRef(), nodeToSave.getValueOffset(), nodeToSave.getLength());
                fos.flush();
                fos.close();
                fos = null;

            } catch (IOException e1)
            {
//                e1.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
        }
    }

    public String selectedToString()
    {
        TreePath selectionPath = jTree.getSelectionPath();
        if (selectionPath == null)
            selectionPath = new TreePath(jTree.getModel().getRoot());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
        TLV nodeToSave = (TLV) node.getUserObject();

        MyPrintingVisitor mpv = new MyPrintingVisitor();
        mpv.traverse(nodeToSave);
        return mpv.buffer.toString();

    }

    class TreeActionListener implements TreeSelectionListener
    {
        private TreeSelectionListener passTru;

        public TreeActionListener(TreeSelectionListener onwards)
        {
            this.passTru = onwards;
        }

        public void valueChanged(TreeSelectionEvent e)
        {
            if (autoScrollToggle.isSelected())
                passTru.valueChanged(e);

            Object selected = e.getPath().getLastPathComponent();
            try
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selected;
                String tagTxt = TagToString.toDisplayableTag((TLV) node.getUserObject(), false);
                statusLabel.setText(tagTxt);
            } catch (Exception e1)
            {

            }
        }

    }

    private class SaveActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            doSaveSelected();
        }
    }

    private class FloatingWinFocusAdapter extends FocusAdapter
    {
        private JFrame win;
        public FloatingWinFocusAdapter(JFrame win)
        {
            super();
            this.win = win;

        }
        public void focusLost(FocusEvent e)
        {
            win.dispose();
//            ((JWindow) e.getSource());.dispose();
        }
    }
}
