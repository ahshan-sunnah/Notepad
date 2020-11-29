
package note_pad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class Note_Pad extends JFrame
{
    static JTextArea mainarea, mainarea1;
    JTextField label, fontText, sizeText, typeText;
    JMenuBar menubar;
    JMenu menuFile, menuEdit, menuFormat, menuFind, menuAbout;
    JMenuItem itemNew, itemOpen, itemSave, itemExit,
              itemCut, itemCopy, itemPaste, itemFont, itemFontColor,
              itemFind, itemReplace;
              
    JButton buttonNew, buttonOpen, buttonSave, buttonFont, buttonReplace, buttonExit;
    String filename;
    String fileContent;
    JFileChooser filesave;
    FontHelper font;
    JToolBar toolbar, toolbar1;
    JScrollPane fontScroll;
    UndoManager undo;
    UndoAction undoAction;
    RedoAction redoAction;
    String findText;
    int fnext = 1;
    int fontSize = 14;
    public static Note_Pad fromMain = new Note_Pad();
    
    public Note_Pad()
    {
        initComponent();
        
        itemNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                New();
            }
        });
        
        itemSave.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                save();
            }
        });
        
        itemOpen.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                open();
            }
        });
        
        itemExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        itemCut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainarea.cut();
            }
        });
        
        itemCopy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainarea.copy();
            }
        });
        
        itemPaste.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainarea.paste();
            }
        });
        
        itemFont.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(true);
            }
        });
        
        font.getOk().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainarea.setFont(font.font());
                font.setVisible(false);
            }
        });
        
        font.getCancel().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(false);
            }
        });
        
        itemFontColor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(rootPane, "Choose Font Color", Color.BLUE);
                mainarea.setForeground(c);
            }
        });
        
        mainarea.getDocument().addUndoableEditListener(new UndoableEditListener() {

            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
                undoAction.update();
                redoAction.update();
            }
        });
        
        itemReplace.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                new FindAndReplace(fromMain, true);
            }
        });
        
        buttonNew.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                New();
            }
        });
        
        buttonOpen.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        
        buttonSave.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        
        buttonFont.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(true);
            }
        });
        
        buttonReplace.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(fromMain, true);;
            }
        });
                
        buttonExit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
    }
    
    private void initComponent()
    {
        final JFrame frame = new JFrame("Word Pad");
        filesave = new JFileChooser(".");
        mainarea = new JTextArea();
        undo = new UndoManager();
        
        
        font = new FontHelper();
        toolbar = new JToolBar();
        
        getContentPane().add(toolbar, BorderLayout.NORTH);
        frame.getContentPane().add(mainarea);
        getContentPane().add(new JScrollPane(mainarea), BorderLayout.CENTER);    
 
        
        //menubar
        menubar = new JMenuBar();
        
        //menu
        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        menuFormat = new JMenu("Format");
        menuFind  = new JMenu("Find & Replace");
        menuAbout = new JMenu("About");
        
        //add icon menuitem
        ImageIcon iconNew = new ImageIcon(getClass().getResource("/icon/new.png"));
        ImageIcon iconOpen = new ImageIcon(getClass().getResource("/icon/open.png"));
        ImageIcon iconSave = new ImageIcon(getClass().getResource("/icon/saveas.png"));
        ImageIcon iconExit = new ImageIcon(getClass().getResource("/icon/cancel.png"));
        ImageIcon iconCut = new ImageIcon(getClass().getResource("/icon/cut.png"));
        ImageIcon iconCopy = new ImageIcon(getClass().getResource("/icon/copy.png"));
        ImageIcon iconPaste = new ImageIcon(getClass().getResource("/icon/paste.png"));
        ImageIcon iconFont = new ImageIcon(getClass().getResource("/icon/font.png"));
        ImageIcon iconFontColor = new ImageIcon(getClass().getResource("/icon/font.png"));
        ImageIcon iconReplace = new ImageIcon(getClass().getResource("/icon/find.png"));
        ImageIcon iconUndo = new ImageIcon(getClass().getResource("/icon/undo.png"));
        ImageIcon iconRedo = new ImageIcon(getClass().getResource("/icon/redo.png"));
        
        
        
        undoAction = new UndoAction(iconUndo);
        redoAction = new RedoAction(iconRedo);
        
        
        buttonNew = new JButton("New",iconOpen);
        buttonOpen = new JButton("Open",iconOpen);
        buttonSave = new JButton("Save",iconSave);
        buttonFont = new JButton("Font",iconFont);
        buttonReplace = new JButton("Find & Replace",iconReplace);
        buttonExit = new JButton("Exit",iconExit);
        
        toolbar.add(buttonNew);
        toolbar.add(buttonOpen);
        toolbar.add(buttonSave);
        toolbar.add(buttonFont);
        toolbar.add(buttonReplace);
        toolbar.add(undoAction);
        toolbar.add(redoAction);
        toolbar.add(buttonExit);
        
        
        //menuitem
        itemNew =  new JMenuItem("New", iconNew);
        itemOpen =  new JMenuItem("Open", iconOpen);
        itemSave =  new JMenuItem("Save", iconSave);
        itemExit =  new JMenuItem("Exit", iconExit);
        itemCut = new JMenuItem("Cut", iconCut);
        itemCopy = new JMenuItem("Copy", iconCopy);
        itemPaste = new JMenuItem("Paste", iconPaste);
        itemFontColor = new JMenuItem("Font Color", iconFontColor);
        itemFont = new JMenuItem("Font", iconFont);
        itemReplace = new JMenuItem("Find & Replace", iconReplace);
        
        
        //menuitem shortcut
        itemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        itemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        itemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        itemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        
        //add item to menu
        menuFile.add(itemNew);
        menuFile.add(itemOpen);
        menuFile.add(itemSave);
        menuFile.add(itemExit);
        
        menuEdit.add(itemCut);
        menuEdit.add(itemCopy);
        menuEdit.add(itemPaste);
        menuEdit.add(undoAction);
        menuEdit.add(redoAction);
        
        menuFormat.add(itemFontColor);
        menuFormat.add(itemFont);
        
        menuFind.add(itemReplace);
        
        
        //add menu to menubar
        menubar.add(menuFile);
        menubar.add(menuEdit);
        menubar.add(menuFormat);
        menubar.add(menuFind);
        
        
        //add menubar to frame
        setJMenuBar(menubar);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void New()
    {
        if (!mainarea.getText().equals("") && !mainarea.getText().equals(fileContent)) {
            if (filename == null) {
                int x = JOptionPane.showConfirmDialog(null, "Do you want save changes?", "Confirm Changes", JOptionPane.OK_CANCEL_OPTION);
                if (x == 0) {
                    save();
                    
                    mainarea.setText("");
                } else {
                    mainarea.setText("");
                    setTitle("Untitled Notepad");
                    filename = null;
                }
            }
            else
            {
                int x = JOptionPane.showConfirmDialog(null, "Do you want save changes?", "Confirm Changes", JOptionPane.OK_CANCEL_OPTION);
                if (x == 0)
                {
                    save();
                    
                    mainarea.setText("");
                    setTitle("Untitled Notepad");
                    filename = null;
                }
                else
                {
                    mainarea.setText("");
                    setTitle("Untitled Notepad");
                    filename = null;
                }
            }
        } 
        else
        {
            mainarea.setText("");
            setTitle("Untitled Notepad");
            filename = null;
        }
    }
    
    private void save() {
        PrintWriter fout = null;
        int return_value = -1;
        try {
            return_value = filesave.showSaveDialog(this);
            if (return_value == JFileChooser.APPROVE_OPTION) 
            {
                if (filesave.getSelectedFile().exists()) 
                {
                    int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to replace this file?", "Confirmation", JOptionPane.OK_CANCEL_OPTION);

                    if (option == 0) 
                    {
                        fout = new PrintWriter(new FileWriter(filesave.getSelectedFile()));
                        String txt = mainarea.getText();
                        StringTokenizer tokn = new StringTokenizer(txt, System.getProperty("line.separator"));
                        while (tokn.hasMoreElements()) {
                            fout.println(tokn.nextToken());
                        }
                        JOptionPane.showMessageDialog(rootPane, "File saved");
                        fileContent = mainarea.getText();
                        filename = filesave.getSelectedFile().getName();
                        setTitle(filename = filesave.getSelectedFile().getName());
                    }
                    else 
                    {
                        save();
                    }
                } 
                else 
                {
                    fout = new PrintWriter(new FileWriter(filesave.getSelectedFile()));
                    String txt = mainarea.getText();
                    StringTokenizer tokn = new StringTokenizer(txt, System.getProperty("line.separator"));
                    while (tokn.hasMoreElements())
                    {
                        fout.println(tokn.nextToken());
                    }
                    JOptionPane.showMessageDialog(rootPane, "File saved");
                    fileContent = mainarea.getText();
                    filename = filesave.getSelectedFile().getName();
                    setTitle(filename = filesave.getSelectedFile().getName());
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        finally 
        {
            if (fout != null) 
            {
                fout.close();
            }
        }
    }
    
    private void open()
    {
        try
        {
            int return_value = filesave.showOpenDialog(this);
            if(return_value == JFileChooser.APPROVE_OPTION)
            {
                mainarea.setText(null);
                Reader input = new FileReader(filesave.getSelectedFile());
                char[] in_char = new char[100000000];
                int in_int;
                while((in_int = input.read(in_char, 0, in_char.length)) != -1)
                {
                    mainarea.append(new String(in_char, 0, in_int));
                }
            }
            filename = filesave.getSelectedFile().getName();
            setTitle(filename = filesave.getSelectedFile().getName());
        }
        catch(IOException ae)
        {
            ae.printStackTrace();
        }
    }
                                          
    
    private void clear() {
        mainarea.setText(null);
        setTitle("Untitled Notepad");
        filename = null;
        fileContent = null;
    }
    
    class UndoAction extends AbstractAction {

        public UndoAction(ImageIcon undoIcon) {
            super("Undo", undoIcon);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, "Undo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }
    
    class RedoAction extends AbstractAction {

        public RedoAction(ImageIcon redoIcon) {
            super("Redo", redoIcon);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, "Redo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Note_Pad NP = new Note_Pad();
        NP.setSize(1650,1080);  
    }
        
    public static JTextArea getArea() {
        return mainarea;
    }
}