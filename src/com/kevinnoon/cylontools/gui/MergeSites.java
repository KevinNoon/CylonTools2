package com.kevinnoon.cylontools.gui;

import com.kevinnoon.cylonapps.Site;
import com.kevinnoon.cylonapps.SiteFunctions;
import com.kevinnoon.cylontools.Master.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.objects.NativeString.trim;

/**
 * Created by kevin on 01/09/2016 at 07:31.
 */
class MergeSites extends JPanel {
    private JComboBox<String> mainSiteList;
    private JComboBox<String> subSiteList;
    private JTextArea messageBox = new JTextArea();
    private JScrollPane messageScroll = new JScrollPane(messageBox);

    private boolean error = false;
    private List<Site> sites = null;

    Map<Integer,Integer> OSMergeList;

    MergeSites() throws ParseException {
        MaskFormatter formatter;
        formatter = new MaskFormatter("?AA**************");
        JFormattedTextField siteNewName = new JFormattedTextField(formatter);

        siteNewName.setValue( "NewSiteName" );

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        mainSiteList = new JComboBox<>();
        subSiteList = new JComboBox<>();

        mainSiteList.addActionListener( event -> {
            if (!error) {
                setSiteLists(sites);
            }
        } );


        mainSiteList.setPreferredSize(new Dimension(200,20));
        c.weightx = 10;
        c.weighty = 10;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets( 0,10,0,0 );
        c.anchor = GridBagConstraints.WEST;
        add(mainSiteList,c);

        subSiteList.setPreferredSize(new Dimension(200,20));
        c.weightx = 10;
        c.weighty = 10;
        c.gridx = 1;
        c.gridy = 1;
        add(subSiteList,c);

        JButton merdgeSites = new JButton( "Merge Sites" );
        merdgeSites.addActionListener( event -> {
            // Check if site exists
            MainPrefs mf = new MainPrefs();
            mf.create();
            String mainPath = mf.prefsGetMainPath();
            try {
                MergeFolder.checkSiteExists( mainPath, trim(siteNewName.getText() ) );
                messageBox.append( "Site Name OK  \n" );
                // Get Folder name
                String newSiteFolder = MergeFolder.checkFolder(mainPath, siteNewName.getText());
                messageBox.append( "Site Folder Name is " + newSiteFolder + "\n");

                if (Paths.get(mainPath + "\\" + newSiteFolder).toFile().exists()){
                    throw new IOException("Unable to create Site Folders as site directory existed");
                }
                SiteFunctions.CreateSiteFolders(Paths.get(mainPath + "\\" + newSiteFolder));
                messageBox.append( "Site Folders Created \n");

        //Create Merge Site.INI
                String MainSitePath = mainPath + "\\" +GetSitePath(sites,mainSiteList.getSelectedItem().toString());
                String SubSitePath = mainPath + "\\" +GetSitePath(sites,subSiteList.getSelectedItem().toString());
                String MergeSitePath = mainPath + "\\" + newSiteFolder;
                OSMergeList = MergeSiteINI.merge(Paths.get( MainSitePath + "\\System\\Site.ini"),Paths.get(SubSitePath + "\\System\\Site.ini"),Paths.get(MergeSitePath + "\\System\\Site.ini"));

                //Copy Folders
                LinkedList<String> copyList = new LinkedList<>();
                 copyList = MergeFolder.copyMaster(MainSitePath,MergeSitePath);
                for (int i = 0; i < copyList.size(); i++) {
                    messageBox.append(copyList.get(i) + "\n");
                }
                copyList = MergeFolder.copySub(OSMergeList,SubSitePath,MergeSitePath);
                for (int i = 0; i < copyList.size(); i++) {
                    messageBox.append(copyList.get(i) + "\n");
                }

//        //Add Assocations to Merge Folder
                String AssociationsXML =  "\\System\\Associations.xml";
                 MergeAssociations.merge(OSMergeList,MainSitePath,SubSitePath,MergeSitePath,AssociationsXML);

                String SiteINI = "\\System\\Site.ini";
                String GlobalsXML = "\\Strat5\\Globals.xml";
              MergeGlobals.merge(MainSitePath,SubSitePath,MergeSitePath,SiteINI,AssociationsXML,GlobalsXML,OSMergeList);




            } catch (IOException e)
            {
                messageBox.append( e.getMessage() + "\n" );
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        } );

        merdgeSites.setPreferredSize(new Dimension(200,20));
        c.weightx = 10;
        c.weighty = 10;
        c.gridx = 1;
        c.gridy = 2;
        add( merdgeSites,c);

        siteNewName.setPreferredSize(new Dimension(200,20));
        c.weightx = 10;
        c.weighty = 10;
        c.gridx = 0;
        c.gridy = 2;
        add(siteNewName,c);

        c.weightx = 100;
        c.weighty = 100;
        c.gridx = 0;
        c.gridy = 3;
        c.ipadx = 10;
        c.gridwidth = 3;
        add(messageScroll,c);
        messageScroll.setPreferredSize( new Dimension(600,200) );
        messageScroll.setBackground(Color.white);
        messageBox.setEditable( false );

        try {
            sites = GetSites();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),"File not found",JOptionPane.ERROR_MESSAGE);
            messageBox.append( "WN3000.ini not found" );
            error = true;
        }

        if (!error) {
            setSiteLists(sites);
        }

    }

    private String GetSitePath(List<Site> sites, String SiteName){
        String sitePath = "";
        for (int x =0; x < sites.size(); x++){
            if (SiteName.equals(sites.get(x).getName())){
                sitePath = sites.get(x).getDirectory();
                break;
            }
        }
        return sitePath;
    }


    private List<Site> GetSites() throws IOException {
        return SiteFunctions.GetSites( Paths.get(MainApp.mainPrefs.prefsGetMainPath() + "\\System\\wn3000.ini"));
    }

    @Override
    public void printAll(Graphics g) {
        super.printAll( g );
    }

    private void setSiteLists(List<Site> sites){
        if (mainSiteList.getItemCount() == 0){
        mainSiteList.removeAllItems();
            for (Site site : sites) {
                mainSiteList.addItem( site.getName() );
            }
        }
        subSiteList.removeAllItems();
        sites.stream().filter( site -> !site.getName().equals( mainSiteList.getSelectedItem() ) ).forEach( site -> subSiteList.addItem( site.getName() ) );
        subSiteList.validate();
    }
}
