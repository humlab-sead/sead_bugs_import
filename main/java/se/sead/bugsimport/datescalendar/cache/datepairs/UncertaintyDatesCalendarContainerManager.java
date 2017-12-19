package se.sead.bugsimport.datescalendar.cache.datepairs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.cache.DatingUncertaintyManager;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UncertaintyDatesCalendarContainerManager {

    private DatingUncertaintyManager uncertaintyManager;
    private NoteBasedPairExtractor dateNoteGroupExtractor;
    private DatingMethodExtractor datingMethodExtractor;

    @Autowired
    public UncertaintyDatesCalendarContainerManager(
            DatingUncertaintyManager uncertaintyManager
    ) {
        this.uncertaintyManager = uncertaintyManager;
        dateNoteGroupExtractor = new NoteBasedPairExtractor();
        datingMethodExtractor = new DatingMethodExtractor();
    }

    public List<DatesCalendarMappingContainer> createRanges(List<BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample) {
        List<DatesCalendarMappingContainer> results = new ArrayList<>();
        Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> groupedByDatingMethod = datingMethodExtractor.extractFrom(carriersForSample);

        for (Map.Entry<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> datingMethodEntry:
                groupedByDatingMethod.entrySet()) {

            Map<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> noteGroups = dateNoteGroupExtractor.extractMappedPairs(datingMethodEntry.getValue());

            for (Map.Entry<String, List<BugsListSeadMapping<DatesCalendar, RelativeDate>>> noteGroup :
                    noteGroups.entrySet()) {

                DatesCalendarMappingContainer caCarriers = getCaCarriers(noteGroup.getValue());
                DatesCalendarMappingContainer nonCaDatesCalendar = getNonCaDatesCalendar(noteGroup.getValue());
                if (caCarriers.rangeShouldBeIncluded()) {
                    results.add(caCarriers);
                }
                if (nonCaDatesCalendar.rangeShouldBeIncluded()) {
                    results.add(nonCaDatesCalendar);
                }
            }
        }
        return results;
    }

    private DatesCalendarMappingContainer getCaCarriers(List<BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample) {
        return new UncertaintyDatePairExtractor(
                new UncertaintyExtractor.FromCaUncertaintyExtractor(uncertaintyManager),
                new UncertaintyExtractor.ToCaUncertaintyExtractor(uncertaintyManager))
                .extractFrom(carriersForSample);
    }

    private DatesCalendarMappingContainer getNonCaDatesCalendar(List<BugsListSeadMapping<DatesCalendar, RelativeDate>> carriersForSample) {
        return new UncertaintyDatePairExtractor(
                new UncertaintyExtractor.FromUncertaintyExtractor(uncertaintyManager),
                new UncertaintyExtractor.ToUncertaintyExtractor(uncertaintyManager))
                .extractFrom(carriersForSample);
    }

    public void updateUncertaintyIfNeeded(RelativeDate originalRelativeDate, boolean isClosedRange) {
        DatingUncertainty uncertainty = originalRelativeDate.getUncertainty();
        if(uncertaintyManager.isCaUncertainty(uncertainty)){
            uncertainty = uncertaintyManager.convertUncertainty(uncertainty);
        } else if(isClosedRange){
            uncertainty = null;
        }
        originalRelativeDate.setUncertainty(uncertainty);
    }

}
