package com.kevinnoon.cylontools.Master;

import com.kevinnoon.cylonapps.CtrlDotNet;
import com.kevinnoon.cylonapps.CtrlSubNet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static com.kevinnoon.cylonapps.CtrlFunctions.*;
import static com.kevinnoon.cylonapps.INIFunctions.*;
import static com.kevinnoon.cylonapps.CtrlFunctions.GetDotNets;
import static com.kevinnoon.cylonapps.CtrlFunctions.GetFreeDotNetTotal;
import static com.kevinnoon.cylonapps.CtrlFunctions.GetSubNetCtrls;
import static jdk.nashorn.internal.objects.NativeString.substring;

/**
 * Created by kevin on 15/06/2016 at 08:39.
 *
 */
public class MergeSiteINI {
    private static Map<Integer,Integer> OldToNewAddress = new HashMap<>();
    private static List< LinkedHashMap<String,String >> OSSections = new ArrayList<>();

    public static Map<Integer,Integer> merge(Path MainSite, Path SubSite, Path NewSite) throws IOException{
        //Need to get path not site name


         ArrayList<CtrlDotNet> mainDotNetList = GetDotNets(MainSite);
        ArrayList<CtrlDotNet> subDotNetList = GetDotNets(SubSite);
        ArrayList<CtrlDotNet> mergeDotNetList = new ArrayList<>();

        ArrayList<CtrlSubNet> mainSubNetList = GetSubNetCtrls(MainSite);
        ArrayList<CtrlSubNet> subSubNetList = GetSubNetCtrls(SubSite);
        ArrayList<CtrlSubNet> subNetListMerge = new ArrayList<>();

        ArrayList<Integer> mainDotNetCtrlsFreeList = GetFreeDotNetTotal(mainDotNetList);
        ArrayList<Integer> subDotNetCtrlsList = GetUsedDotNets(subDotNetList);

        if (mainDotNetCtrlsFreeList.size() <= subDotNetCtrlsList.size()) {
            return OldToNewAddress;
        }

        for (int n = 0; n < subDotNetList.size(); n++) {
            OldToNewAddress.put(subDotNetCtrlsList.get(n),mainDotNetCtrlsFreeList.get(n));
        }

        mergeDotNetList.addAll(mainDotNetList);
        subNetListMerge.addAll(mainSubNetList);

        for (int n = 0; n < OldToNewAddress.size(); n++){
            CtrlDotNet ctrlDotNet;
            ctrlDotNet = subDotNetList.get(n);
            int address = ctrlDotNet.getAddress();
            address = OldToNewAddress.get(address);
            ctrlDotNet.setAddress(address);
            String dotNetName = ctrlDotNet.getName();
            boolean isNumeric = ctrlDotNet.getName().substring( 0,3 ).chars().allMatch( Character::isDigit );
            if (isNumeric){
                dotNetName = dotNetName.substring( 3 );
            }
            String dotNetNo = "00" + address;
            dotNetNo = dotNetNo.substring( dotNetNo.length()-3);
            dotNetName = dotNetNo + dotNetName;
            ctrlDotNet.setName( dotNetName );
            mergeDotNetList.add(ctrlDotNet);
        }

        for (CtrlSubNet aSubSubNetList : subSubNetList) {
            CtrlSubNet ctrlSubNet;
            ctrlSubNet = aSubSubNetList;
            int address = ctrlSubNet.getDotNetID();
            address = OldToNewAddress.get(address);
            ctrlSubNet.setDotNetID(address);
            subNetListMerge.add(ctrlSubNet);
        }

        // Create Sections

        int OSAddress;
        int OSMaxAddress = 0;
        for (CtrlDotNet aMergeDotNetList : mergeDotNetList) {
            LinkedHashMap<String, String> SectionMap = new LinkedHashMap<>();
            OSAddress = aMergeDotNetList.getAddress();
            if (OSMaxAddress < OSAddress) OSMaxAddress = OSAddress;
            SectionMap.put("SectionName", "[OS" + OSAddress + "]");
            SectionMap.put("Name", aMergeDotNetList.getName());
            SectionMap.put("Type", String.valueOf(aMergeDotNetList.getType()));
            if (!(aMergeDotNetList.getBacNetID() == 0)) {
                SectionMap.put("BacNetID", String.valueOf(aMergeDotNetList.getBacNetID()));
            }
            if (!(aMergeDotNetList.getSubNetIndex() == 0)) {
                SectionMap.put("BacNetID", String.valueOf(aMergeDotNetList.getSubNetIndex()));
            }
            Integer MaxUC16 = 0;
            for (CtrlSubNet aSubNetListMerge1 : subNetListMerge) {
                if (aSubNetListMerge1.getDotNetID() == OSAddress) {
                    if (MaxUC16 < aSubNetListMerge1.getID()) {
                        MaxUC16 = aSubNetListMerge1.getID();
                    }
                }
                SectionMap.put("MaxUC16", String.valueOf(MaxUC16));
            }
            for (CtrlSubNet aSubNetListMerge : subNetListMerge) {
                if (aSubNetListMerge.getDotNetID() == OSAddress) {
                    SectionMap.put("UC16_" + aSubNetListMerge.getID(), aSubNetListMerge.getName());
                    SectionMap.put("UC16Type_" + aSubNetListMerge.getID(), String.valueOf(aSubNetListMerge.getType()));
                    if (!(subNetListMerge.get(0).getBacNetID() == 0)) {
                        SectionMap.put("UC16_BACnetDeviceID" + aSubNetListMerge.getBacNetID(), String.valueOf(aSubNetListMerge.getBacNetID()));
                    }
                }
            }
            OSSections.add(SectionMap);
        }

        File file = NewSite.toFile();
        boolean result = file.createNewFile();
        if (!result) return OldToNewAddress;

        LinkedHashMap<String, String> CCConfig = new LinkedHashMap<>();
        CCConfig.put("SectionName","[CCConfig]");
        CCConfig.put("MaxOutstation", String.valueOf(OSMaxAddress));

        AddSelection(NewSite,CCConfig,true);

        for (LinkedHashMap<String, String> OSSection : OSSections) {
            AddOSSection(NewSite, OSSection);
        }

        HashMap<String,String>  DigitalUnits;
        HashMap<String,String>  AnalogUnits;
        HashMap<String,String>  CCAlarms;

        DigitalUnits = GetSection(MainSite,"[DigitalUnits]");
        AnalogUnits = GetSection(MainSite,"[AnalogUnits]");
        CCAlarms = GetSection(MainSite,"[CCAlarms]");

        if (!DigitalUnits.isEmpty()) AddSelection(NewSite,DigitalUnits,false);
        if (!AnalogUnits.isEmpty()) AddSelection(NewSite,AnalogUnits,false);

        if (CCAlarms.size() > 0) AddSelection(NewSite,CCAlarms,false);
       return OldToNewAddress;
    }
}