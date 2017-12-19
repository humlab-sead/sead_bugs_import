package se.sead.bugsimport.datescalendar.cache.datepairs;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.List;

class UncertaintyDatePairExtractor {
    private UncertaintyExtractor fromExtractor;
    private UncertaintyExtractor toExtractor;

    UncertaintyDatePairExtractor(UncertaintyExtractor fromExtractor, UncertaintyExtractor toExtractor) {
        this.fromExtractor = fromExtractor;
        this.toExtractor = toExtractor;
    }

    DatesCalendarMappingContainer extractFrom(List<BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample) {
        BugsListSeadMapping<DatesCalendar, RelativeDate> from = fromExtractor.getForUncertainty(carriersForSample);
        BugsListSeadMapping<DatesCalendar, RelativeDate> to = toExtractor.getForUncertainty(carriersForSample);
        return new DatesCalendarMappingContainer(from, to);
    }
}
