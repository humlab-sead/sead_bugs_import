package se.sead.bugsimport.datescalendar.cache.datepairs;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class NoteBasedPairExtractor {

    private static final String DEFAULT_MAPPING = "";

    private Pattern noteMatchingPattern = Pattern.compile(".*Date [0-9]?");


    public Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> extractMappedPairs(List<BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample){
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> mappedItems = new HashMap<>();
        mappedItems.put(DEFAULT_MAPPING, new ArrayList<>());
        for (BugsListSeadMapping<DatesCalendar, RelativeDate> sampleMapping :
                carriersForSample) {
            String note = sampleMapping.getBugsData().getNotes();
            if(containMarkerNote(note)){
                List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappingsForNote = mappedItems.get(note);
                if(mappingsForNote == null){
                    mappingsForNote = new ArrayList<>();
                    mappedItems.put(note, mappingsForNote);
                }
                mappingsForNote.add(sampleMapping);
            } else {
                mappedItems.get("").add(sampleMapping);
            }
        }
        return mappedItems;
    }

    private boolean containMarkerNote(String bugsNote){
        if(bugsNote != null){
            return noteMatchingPattern.matcher(bugsNote).matches();
        }
        return false;
    }
}
