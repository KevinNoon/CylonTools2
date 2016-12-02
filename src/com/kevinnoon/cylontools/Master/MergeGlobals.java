package com.kevinnoon.cylontools.Master;

import com.kevinnoon.cylonapps.GlobalHeader;
import com.kevinnoon.cylonapps.GlobalsFunctions;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import static com.kevinnoon.cylonapps.GlobalXMLFunctions.*;
import static com.kevinnoon.cylonapps.GlobalXMLFunctions.*;
import static com.kevinnoon.cylonapps.GlobalsFunctions.setNewDotNetNo;
import static com.kevinnoon.cylonapps.GlobalsFunctions.setNewGlobalNo;


/**
 * Created by kevin on 24/06/2016 at 07:54.
 *
 */
public class MergeGlobals {
    public static void merge(String SiteRootMaster,String SiteRootSub,String SiteRootMerge,String SiteINI, String AssociationsXML,String GlobalsXML, Map<Integer,Integer> OSMergeList) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        ////Create the new global file
        CreateNewXML(Paths.get(SiteRootMerge + GlobalsXML));

        //Get the Master file
        List<GlobalHeader> GlocalList = GetGlobals(Paths.get(SiteRootMaster + GlobalsXML ));

        //Add the master table to the new file
        globalType gt;
        for (GlobalHeader aGlocalList : GlocalList) {

            switch (aGlocalList.getClass().getSimpleName()) {
                case "GlobalLocal":
                    gt = globalType.local;
                    break;
                case "GlobalSmart":
                    gt = globalType.smart;
                    break;
                case "GlobalTemporary":
                    gt = globalType.temporary;
                    break;
                case "GlobalWideDestination":
                    gt = globalType.wideDestination;
                    break;
                case "GlobalWideSource":
                    gt = globalType.wideSource;
                    break;
                default:
                    gt = globalType.local;
            }
            if (!isDotNetUsed(Paths.get(SiteRootMerge + GlobalsXML), aGlocalList.getDotNetNo())) {
                addDotNet(Paths.get(SiteRootMerge + GlobalsXML), aGlocalList.getDotNetNo());
                addGlobal(Paths.get(SiteRootMerge + GlobalsXML),aGlocalList,gt);
            } else {

                addGlobal(Paths.get(SiteRootMerge + GlobalsXML),aGlocalList,gt);
            }
        }

        //Get Sub File

        List<GlobalHeader> GlocalListSub = GetGlobals(Paths.get( SiteRootSub + GlobalsXML ));

        // Set the new DotNet number
        setNewDotNetNo(GlocalListSub,OSMergeList);


        //Set the new Wide Global numbers

        ArrayList<Integer> rootGlobal = new ArrayList<>(  );
        ArrayList<Integer> subGlobal = new ArrayList<>(  );
        rootGlobal = GlobalsFunctions.getUsedGlobal( GlocalList );
        subGlobal = GlobalsFunctions.getUsedGlobal( GlocalListSub );
        Map<Integer,Integer> globalChangeList;
        globalChangeList = GlobalsFunctions.getWideGlobalChange( rootGlobal,subGlobal);
        GlobalsFunctions.setNewGlobalNo( GlocalListSub,globalChangeList );


//Copy the Sub Globals to the new file
        for (GlobalHeader aGlocalListSub : GlocalListSub) {
            switch (aGlocalListSub.getClass().getSimpleName()) {
                case "GlobalLocal":
                    gt = globalType.local;
                    break;
                case "GlobalSmart":
                    gt = globalType.smart;
                    break;
                case "GlobalTemporary":
                    gt = globalType.temporary;
                    break;
                case "GlobalWideDestination":
                    gt = globalType.wideDestination;
                    break;
                case "GlobalWideSource":
                    gt = globalType.wideSource;
                    break;
                default:
                    gt = globalType.local;
            }
            if (!isDotNetUsed(Paths.get(SiteRootMerge + GlobalsXML), aGlocalListSub.getDotNetNo())) {
                addDotNet(Paths.get(SiteRootMerge + GlobalsXML), aGlocalListSub.getDotNetNo());
                addGlobal(Paths.get(SiteRootMerge + GlobalsXML),aGlocalListSub,gt);
            } else {
                addGlobal(Paths.get(SiteRootMerge + GlobalsXML),aGlocalListSub,gt);
            }
        }
    }
}

