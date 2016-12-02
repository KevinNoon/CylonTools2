package com.kevinnoon.cylontools.gui;

import com.kevinnoon.cylontools.Master.MainPrefs;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by kevin on 01/09/2016 at 07:22.
 */
public class MainApp {
    public static MainPrefs mainPrefs = new MainPrefs();
    public static void main(String[] args) {

        mainPrefs.create();
        // TODO Auto-generated method stub
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                try {
                    new MainFrame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});
    }
}
