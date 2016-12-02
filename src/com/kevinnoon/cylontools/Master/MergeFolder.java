package com.kevinnoon.cylontools.Master;

import com.kevinnoon.cylonapps.FileOperations;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.util.*;

import static com.kevinnoon.cylonapps.INIFunctions.GetSection;
import static java.lang.Math.min;

/**
 * Created by kevin on 17/06/2016 at 19:44.
 */
public class MergeFolder {

    public static  void checkSiteExists(String mainPath, String newSiteName) throws IOException{
        LinkedHashMap<String ,String > Sites  = null;
            Sites = GetSection( Paths.get(mainPath + "\\System\\WN3000.ini"),"SiteList");
        for (int i = 1; i <= Integer.parseInt(Sites.get("TotalSites")); i++) {
                String siteName = GetSection(Paths.get(mainPath + "\\System\\WN3000.ini"),"[Site" + Sites.get("Site" + i) + "]").get("Name");
                if (siteName.equals( newSiteName) ) throw new FileAlreadyExistsException( "Site Exists" );
        }
    }

    public static String checkFolder(String SiteRootMaster, String siteName) throws IOException{
        String siteFolder;
        siteFolder = siteName.substring( 0, min(8,siteName.length()));
        if (new File(SiteRootMaster  + "\\" + siteFolder).exists()){
            for (int i = 1; i <= 100; i++) {
                if (i == 100)throw new FileAlreadyExistsException( "Unable to create folder" );
                if (i < 10){siteFolder = siteFolder.substring( 0, min(7,siteName.length()) ) + i;}
                else {siteFolder = siteFolder.substring( 0, min(6,siteName.length()) ) + i;}
                if  (!(new File(SiteRootMaster   + "\\" + siteFolder).exists())) break;
            }
        }
        return siteFolder;
    }

    public static LinkedList<String>  copyMaster(String SiteRootMaster, String SiteRootMerge) throws IOException {
        LinkedList<String> copyList = new LinkedList<>();
        copyList = FileOperations.copyFolder(Paths.get(SiteRootMaster + "//DBase").toFile(), Paths.get(SiteRootMerge + "//DBase").toFile());
        LinkedList<String> copyListA = new LinkedList<>();
        copyListA = FileOperations.copyFolder(Paths.get(SiteRootMaster + "//Strat5").toFile(), Paths.get(SiteRootMerge + "//Strat5").toFile());
        for (int i = 0; i < copyListA.size() ; i++) {
            copyList.add(copyListA.get(i));
        }
        return copyList;
    }

    public static LinkedList<String> copySub(Map<Integer, Integer> OSMergeList, String SiteRootSub, String SiteRootMerge) throws IOException {
        LinkedList<String> copyList = new LinkedList<>();
        Set setFolders = OSMergeList.entrySet();
        Iterator iteratorFolders = setFolders.iterator();
        String MasterStratDir = "";
        String SubStratDir;
        while (iteratorFolders.hasNext()) {
            Map.Entry me = (Map.Entry) iteratorFolders.next();
            MasterStratDir = ("00" + me.getKey().toString());
            MasterStratDir = MasterStratDir.substring(MasterStratDir.length() - 3);
            SubStratDir = ("00" + me.getValue().toString());
            SubStratDir = SubStratDir.substring(SubStratDir.length() - 3);

            copyList = FileOperations.copyFolder(Paths.get(SiteRootSub + "//DBase//" + me.getKey()).toFile(), Paths.get(SiteRootMerge + "//DBase//" + me.getValue()).toFile());

            LinkedList<String> copyListA = new LinkedList<>();
            copyListA = (FileOperations.copyFolder(Paths.get(SiteRootSub + "//Strat5//" + MasterStratDir).toFile(), Paths.get(SiteRootMerge + "//Strat5//" + SubStratDir).toFile()));
            for (int i = 0; i < copyListA.size() ; i++) {
                copyList.add(copyListA.get(i));
            }
        }
        return copyList;
    }
}
