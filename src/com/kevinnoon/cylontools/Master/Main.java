package com.kevinnoon.cylontools.Master;


import com.kevinnoon.cylonapps.SiteFunctions;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;


/**
 * Created by kevin on 22/03/2016.
 *
 */

public class Main {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {


        String SiteRootMaster = "C:\\temp\\AA\\APPLUNIT";
        String SiteRootSub = "C:\\temp\\AA\\APPLUNIT";
        String SiteRootMerge = "C:\\temp\\AA\\Merge";
        String SiteINI = "\\System\\Site.ini";
        String AssociationsXML =  "\\System\\Associations.xml";
        String GlobalsXML = "\\Strat5\\Globals.xml";
        Map<Integer,Integer> OSMergeList;
        //Create Site
        SiteFunctions.CreateSiteFolders(Paths.get(SiteRootMerge));
//
//        //Create Merge Site.INI
       OSMergeList = MergeSiteINI.merge(Paths.get(SiteRootMaster + SiteINI),Paths.get(SiteRootSub + SiteINI),Paths.get(SiteRootMerge + SiteINI));
//
//        //Copy Folders
        MergeFolder.copyMaster(SiteRootMaster,SiteRootMerge);
        MergeFolder.copySub(OSMergeList,SiteRootSub,SiteRootMerge);
//
//        //Add Assocations to Merge Folder
//        MergeAssociations.merge(OSMergeList,SiteRootMaster,SiteRootSub,SiteRootMerge,AssociationsXML);
//
//        MergeGlobals.merge(SiteRootMaster,SiteRootSub,SiteRootMerge,SiteINI,AssociationsXML,GlobalsXML,OSMergeList);


       //TODO Get list of sites for WN3000.ini
        //TODO at new site to WN3000.ini


    }
}

