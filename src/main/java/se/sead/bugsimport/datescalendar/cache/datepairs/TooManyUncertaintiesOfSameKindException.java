package se.sead.bugsimport.datescalendar.cache.datepairs;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.List;

public class TooManyUncertaintiesOfSameKindException extends RuntimeException {
    private List<BugsListSeadMapping<DatesCalendar, RelativeDate>> foundMappingsWithSameKindOfUncertainty;
    TooManyUncertaintiesOfSameKindException(
            List<BugsListSeadMapping<DatesCalendar, RelativeDate>> foundMappingsWithSameKindOfUncertainty
    ){
        this.foundMappingsWithSameKindOfUncertainty = foundMappingsWithSameKindOfUncertainty;
    }

    public List<BugsListSeadMapping<DatesCalendar, RelativeDate>> getFoundMappingsWithSameKindOfUncertainty() {
        return foundMappingsWithSameKindOfUncertainty;
    }
}
