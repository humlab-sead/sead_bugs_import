package se.sead.bugsimport.datescalendar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;

import java.util.ArrayList;
import java.util.List;

@Component
public class UncertaintyDatesCalendarContainerManager {

    private DatingUncertaintyManager uncertaintyManager;

    @Autowired
    public UncertaintyDatesCalendarContainerManager(
            DatingUncertaintyManager uncertaintyManager
    ) {
        this.uncertaintyManager = uncertaintyManager;
    }

    List<UncertaintyDatesCalendarContainer> createRanges(List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample) {
        UncertaintyDatesCalendarContainer caDates = getCaCarriers(carriersForSample);
        UncertaintyDatesCalendarContainer nonCaDates = getNonCaDatesCalendar(carriersForSample);
        List<UncertaintyDatesCalendarContainer> results = new ArrayList<>();
        if (caDates.rangeShouldBeIncluded()) {
            results.add(caDates);
        }
        if (nonCaDates.rangeShouldBeIncluded()) {
            results.add(nonCaDates);
        }
        return results;
    }

    private UncertaintyDatesCalendarContainer getCaCarriers(List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample) {
        return new Extractor(
                new UncertaintyExtractor.FromCaUncertaintyExtractor(uncertaintyManager),
                new UncertaintyExtractor.ToCaUncertaintyExtractor(uncertaintyManager))
                .extractFrom(carriersForSample);
    }

    private UncertaintyDatesCalendarContainer getNonCaDatesCalendar(List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample) {
        return new Extractor(
                new UncertaintyExtractor.FromUncertaintyExtractor(uncertaintyManager),
                new UncertaintyExtractor.ToUncertaintyExtractor(uncertaintyManager))
                .extractFrom(carriersForSample);
    }

    void updateUncertaintyIfNeeded(RelativeDate originalRelativeDate, boolean isClosedRange) {
        DatingUncertainty uncertainty = originalRelativeDate.getUncertainty();
        if(uncertaintyManager.isCaUncertainty(uncertainty)){
            uncertainty = uncertaintyManager.convertUncertainty(uncertainty);
        } else if(isClosedRange){
            uncertainty = null;
        }
        originalRelativeDate.setUncertainty(uncertainty);
    }

    private static class Extractor {
        private UncertaintyExtractor fromExtractor;
        private UncertaintyExtractor toExtractor;

        Extractor(UncertaintyExtractor fromExtractor, UncertaintyExtractor toExtractor) {
            this.fromExtractor = fromExtractor;
            this.toExtractor = toExtractor;
        }

        UncertaintyDatesCalendarContainer extractFrom(List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample) {
            MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> from = fromExtractor.getForUncertainty(carriersForSample);
            MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> to = toExtractor.getForUncertainty(carriersForSample);
            return new UncertaintyDatesCalendarContainer(from, to);
        }
    }
}
