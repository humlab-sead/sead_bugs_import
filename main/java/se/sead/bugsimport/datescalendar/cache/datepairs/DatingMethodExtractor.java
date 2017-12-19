package se.sead.bugsimport.datescalendar.cache.datepairs;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DatingMethodExtractor {

    private static final String EMPTY_METHOD_GROUP = "";

    public Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> extractFrom(List<BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample){
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> datingMethodGroups = new HashMap<>();
        datingMethodGroups.put(EMPTY_METHOD_GROUP, new ArrayList<>());
        for (BugsListSeadMapping<DatesCalendar, RelativeDate> mapping:
                carriersForSample
             ) {
            String datingMethod = mapping.getBugsData().getDatingMethod();
            if(datingMethod == null || datingMethod.isEmpty()){
                datingMethodGroups.get(EMPTY_METHOD_GROUP).add(mapping);
            } else {
                List<BugsListSeadMapping<DatesCalendar, RelativeDate>> existingMappingForDatingMethod = datingMethodGroups.get(datingMethod);
                if(existingMappingForDatingMethod == null){
                    existingMappingForDatingMethod = new ArrayList<>();
                    datingMethodGroups.put(datingMethod, existingMappingForDatingMethod);
                }
                existingMappingForDatingMethod.add(mapping);
            }
        }
        return datingMethodGroups;
    }
}
