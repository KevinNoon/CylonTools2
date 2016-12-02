package com.kevinnoon.cylontools.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by kevin on 01/09/2016 at 07:23.
 */
public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private static final long serialVersionUID = 1L;
    public MainFrame() throws IOException {
        super("Unitron Tools");
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent arg0) {
                dispose();
                System.gc();
            }
        });

        setJMenuBar(createMenuBar());

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(840, 640));
        setMinimumSize(new Dimension(600, 400));
        setVisible(true);
        pack();
        setLocationRelativeTo( null );
    }

    private JMenuBar createMenuBar() throws IOException {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu profileMenu = new JMenu("Options");

        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.addSeparator();
        fileMenu.add(exit);

        JMenuItem newProfile = new JMenuItem("Set");
        tabbedPane = new JTabbedPane();

        profileMenu.add(newProfile);

        menuBar.add(fileMenu);
        menuBar.add(profileMenu);
        MergeSites mergeSites = null;
        try {
            mergeSites = new MergeSites();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tabbedPane.addTab("MergeSites", mergeSites);
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        newProfile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                OptionsForm optionsForm =  new OptionsForm();
           //     optionsForm.setModal( true );
               optionsForm.setAlwaysOnTop( true );
            };
        });

        newProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                ActionEvent.CTRL_MASK));

        fileMenu.setMnemonic(KeyEvent.VK_F);
        profileMenu.setMnemonic(KeyEvent.VK_P);

        exit.setMnemonic(KeyEvent.VK_X);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.CTRL_MASK));
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int action = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Do you realy what to quit?", "comfirm Quit",
                        JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    WindowListener[] listeners = getWindowListeners();
                    for (WindowListener listener : listeners){
                        listener.windowClosing(new WindowEvent(MainFrame.this,0));
                    }

                }

            }
        });
        return menuBar;

    }
}
