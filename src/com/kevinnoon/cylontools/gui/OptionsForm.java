package com.kevinnoon.cylontools.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.kevinnoon.cylontools.gui.MainApp.mainPrefs;

/**
 * Created by kevin on 06/09/2016 at 16:29.
 */
public class OptionsForm extends JDialog{
    public OptionsForm (){
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent arg0) {
                dispose();
                System.gc();
            }
        });

        final JFileChooser chooser = new JFileChooser();
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize( new Dimension(600, 400));
        setResizable( false );
        setVisible(true);



        JTextField rootPath = new JTextField(  );
        rootPath.setText( mainPrefs.prefsGetMainPath() );
        rootPath.setEditable( false );
        rootPath.setPreferredSize( new Dimension( 160,20 ) );
        c.weightx = 10;
        c.weighty = 10;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets( 0,10,0,0 );
        c.anchor = GridBagConstraints.WEST;
        add( rootPath ,c);

        JButton getRootDirectory = new JButton("Set Root");
        getRootDirectory.setPreferredSize(new Dimension(200,20));

        JButton btnSave = new JButton("Save");
        btnSave.setPreferredSize(new Dimension(200,20));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(200,20));

        getRootDirectory.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                chooser.setCurrentDirectory(new java.io.File(rootPath.getText()));
                chooser.setDialogTitle("Select Root");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog( OptionsForm.this ) == JFileChooser.APPROVE_OPTION) {
                    rootPath.setText(chooser.getSelectedFile().getPath());
                }
            }});

        btnSave.addActionListener( e -> {
            mainPrefs.prefsPutMainPath(rootPath.getText() );
            dispose();
        } );

        btnCancel.addActionListener( e -> dispose() );

        c.gridx = 0;
        add(getRootDirectory,c);

        c.gridx = 0;
        c.gridy = 2;
        add(btnSave,c);

        c.gridx = 1;
        c.gridy = 2;
        add(btnCancel,c);


        pack();
        setLocationRelativeTo( null );
    }

}
