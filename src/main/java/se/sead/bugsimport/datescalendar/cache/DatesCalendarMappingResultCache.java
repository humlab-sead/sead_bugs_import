package se.sead.bugsimport.datescalendar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;

import java.util.*;
import java.util.stream.Collectors;

public class DatesCalendarMappingResultCache {

    private DatingUncertaintyManager uncertaintyManager;
    private List<UncertaintyCache> uncertaintyCaches;

    @Autowired
    public DatesCalendarMappingResultCache(
            DatingUncertaintyManager uncertaintyManager
    ){
        this.uncertaintyManager = uncertaintyManager;
        uncertaintyCaches = Arrays.asList(
                new UncertaintyCache(null)
        );
    }

    public void add(DatesCalendar datesCalendar, RelativeDate seadData){
        for (UncertaintyCache cache :
                uncertaintyCaches) {
            if (cache.add(datesCalendar, seadData)){
                return;
            }
        }
        throw new UnsupportedOperationException("no known uncertainty for date: " + seadData.getUncertainty());
    }

    public List<RelativeDate> getDatesFor(DatesCalendar datesCalendar){
        return uncertaintyCaches.stream()
                .map(cache -> cache.getDateForSampleCode(datesCalendar.getSample()))
                .filter(foundResult -> foundResult.isPresent())
                .map(foundResult -> foundResult.get())
                .collect(Collectors.toList());
    }

    private static class UncertaintyCache {
        private DatingUncertainty uncertainty;
        private Map<String, RelativeDate> relativeDatesForSampleCodes;

        UncertaintyCache(DatingUncertainty uncertainty){
            this.uncertainty = uncertainty;
            relativeDatesForSampleCodes = new HashMap<>();
        }

        DatingUncertainty getUncertainty(){
            return uncertainty;
        }

        boolean add(DatesCalendar bugsData, RelativeDate relativeDate) {
            if(isSupportedUncertainty(relativeDate.getUncertainty())){
                relativeDatesForSampleCodes.put(bugsData.getSample(), relativeDate);
                return true;
            }
            return false;
        }

        protected boolean isSupportedUncertainty(DatingUncertainty suppliedUncertainty){
            return true;
        }

        Optional<RelativeDate> getDateForSampleCode(String sampleCode){
            if(relativeDatesForSampleCodes.containsKey(sampleCode)){
                return Optional.of(relativeDatesForSampleCodes.get(sampleCode));
            }
            return Optional.empty();
        }
    }
}
