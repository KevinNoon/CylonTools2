package com.kevinnoon.cylontools.Master;

import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.kevinnoon.cylonapps.AssociationsFunctions.*;

/**
 * Created by kevin on 17/06/2016 at 07:13.
 *
 */
public class MergeAssociations {

    public static void merge(Map<Integer, Integer> OSMergeList, String SiteRootMaster, String SiteRootSub, String SiteRootMerge, String AssociationsXML) {

        //Copy Master Site
        LinkedHashMap<Integer, LinkedHashMap<Integer, String>> assListMaster;
        assListMaster = GetAssociations(Paths.get(SiteRootMaster + AssociationsXML));
        System.out.println(SiteRootMaster);
        System.out.println(assListMaster);

        CreateNewXML(Paths.get(SiteRootMerge + AssociationsXML));
        Set setMaster = assListMaster.entrySet();
        Iterator iteratorMaster = setMaster.iterator();
        while (iteratorMaster.hasNext()) {
            Map.Entry me = (Map.Entry) iteratorMaster.next();
            int x = AddDotNet(Paths.get(SiteRootMerge + AssociationsXML), (LinkedHashMap<Integer, String>) me.getValue(), (Integer) me.getKey());
        }

        LinkedHashMap<Integer, LinkedHashMap<Integer, String>> assListSub;
        assListSub = GetAssociations(Paths.get(SiteRootSub + AssociationsXML));

        //Copy Sub Site with new DotNet Numbers
        Set setSub = assListSub.entrySet();
        Iterator iteratorSub = setSub.iterator();
        while (iteratorSub.hasNext())

        {
            Map.Entry me = (Map.Entry) iteratorSub.next();
            int OSNo = OSMergeList.get((Integer) me.getKey());
            int x = AddDotNet(Paths.get(SiteRootMerge + AssociationsXML), (LinkedHashMap<Integer, String>) me.getValue(), OSNo);
        }
    }
}
