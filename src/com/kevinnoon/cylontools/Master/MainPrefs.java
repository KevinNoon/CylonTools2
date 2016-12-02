package com.kevinnoon.cylontools.Master;

import java.util.prefs.Preferences;

/**
 * Created by kevin on 09/09/2016 at 07:24.
 *
 */

public class MainPrefs {
    // declare my variable at the top of my Java class
    private Preferences prefs;
 public  void create() {
// create a MainPrefs instance (somewhere later in the code)
     prefs = Preferences.userRoot().node(this.getClass().getName());
 }
 public void prefsPutMainPath(String rootPath){
     prefs.put( "MainPath", rootPath );
}
    public String prefsGetMainPath(){
        return prefs.get( "MainPath","C:\\UnitronUC32" );
    }
}
