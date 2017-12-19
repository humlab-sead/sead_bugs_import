package se.sead.bugsimport.datescalendar.cache;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;

import java.util.*;

public class RelativeDatesMappingCache {

    private DatingUncertaintyManager uncertaintyManager;

    private Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> mergeablePeriodDates;
    private Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> nonMergeablePeriodDates;

    RelativeDatesMappingCache(DatingUncertaintyManager uncertaintyManager){
        this.uncertaintyManager = uncertaintyManager;
        mergeablePeriodDates = new HashMap<>();
        nonMergeablePeriodDates = new HashMap<>();
    }

    public void add(BugsListSeadMapping<DatesCalendar, RelativeDate> mapping){
        if(isMergeableMapping(mapping)){
            add(mergeablePeriodDates, mapping);
        } else {
            add(nonMergeablePeriodDates, mapping);
        }
    }

    private boolean isMergeableMapping(BugsListSeadMapping<DatesCalendar, RelativeDate> mapping) {
        if(mapping.isErrorFree() || mapping.isNewSeadData()){
            DatingUncertainty uncertainty = mapping.getSeadData().get(0).getUncertainty();
            if(uncertainty != null) {
                return uncertaintyManager.isToOrFromUncertaintyWithoutCaValidation(uncertainty);
            }
        }
        return false;
    }

    private void add(Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> container, BugsListSeadMapping<DatesCalendar, RelativeDate> mapping) {
        String sampleCODE = mapping.getBugsData().getSample();
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappingForSample = container.get(sampleCODE);
        if(mappingForSample == null){
            mappingForSample = new ArrayList<>();
            container.put(sampleCODE, mappingForSample);
        }
        mappingForSample.add(mapping);
    }

    public Set<String> getStoredSampleCodes(){
        Set<String> sampleCodes = new HashSet<>();
        sampleCodes.addAll(mergeablePeriodDates.keySet());
        sampleCodes.addAll(nonMergeablePeriodDates.keySet());
        return sampleCodes;
    }

    public List<BugsListSeadMapping<DatesCalendar, RelativeDate>> getMergeablePeriodDates(String sampleCODE) {
        if(mergeablePeriodDates.containsKey(sampleCODE)){
            return mergeablePeriodDates.get(sampleCODE);
        }
        return Collections.EMPTY_LIST;
    }

    public List<BugsListSeadMapping<DatesCalendar, RelativeDate>> getNonMergeablePeriodDates(String sampleCODE) {
        if(nonMergeablePeriodDates.containsKey(sampleCODE)) {
            return nonMergeablePeriodDates.get(sampleCODE);
        }
        return Collections.EMPTY_LIST;
    }
}
