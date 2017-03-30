package se.sead.bugsimport.datescalendar.cache;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.util.ArrayList;
import java.util.List;

@Component
public class RelativeDateMerger {

    private UncertaintyDatesCalendarContainerManager ageMerger;
    private RelativeRangeAgeManager ageManager;

    public RelativeDateMerger(
            UncertaintyDatesCalendarContainerManager ageMerger,
            RelativeRangeAgeManager ageManager) {
        this.ageMerger = ageMerger;
        this.ageManager = ageManager;
    }

    public List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mergeItems(
            List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mergeableItems
    ) {
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mergedItems = new ArrayList<>();
        List<UncertaintyDatesCalendarContainer> ranges = ageMerger.createRanges(mergeableItems);
        for (UncertaintyDatesCalendarContainer container:
                ranges) {
            RelativeAge mergeRelativeAge = ageManager.createOrGet(container.getFromDate(), container.getToDate());
            mergedItems.add(updateAndExtractMapping(container, mergeRelativeAge));
        };
        return mergedItems;
    }

    private MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> updateAndExtractMapping(
            UncertaintyDatesCalendarContainer container,
            RelativeAge mergeRelativeAge
    ) {
        InformationCopier copier = new InformationCopier(ageMerger, container, mergeRelativeAge);
        return copier.updateMapping();
    }

    private static class InformationCopier {

        private UncertaintyDatesCalendarContainerManager ageMerger;
        private UncertaintyDatesCalendarContainer container;
        private RelativeAge range;
        private boolean useFromAsDefault = true;

        public InformationCopier(UncertaintyDatesCalendarContainerManager ageMerger, UncertaintyDatesCalendarContainer container, RelativeAge range) {
            this.ageMerger = ageMerger;
            this.container = container;
            this.range = range;
            useFromAsDefault = container.getFromDate().getUncertainty() != null;
        }

        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> updateMapping(){
            setRelativeAge();
            updateUncertainty();
            mergeNotes();
            return useFromAsDefault ? container.getFromMapping() : container.getToMapping();
        }

        private void setRelativeAge() {
            getTargetDate().setRelativeAge(range);
        }

        private RelativeDate getTargetDate(){
            if(useFromAsDefault){
                return container.getFromMapping().getSeadData().get(0);
            } else {
                return container.getToMapping().getSeadData().get(0);
            }
        }

        private void updateUncertainty() {
            ageMerger.updateUncertaintyIfNeeded(getTargetDate(), isClosedRange());
        }

        private boolean isClosedRange(){
            return container.getFromMapping() != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY &&
                    container.getToMapping() != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY;
        }

        private void mergeNotes() {
            if(container.canTryToMergeDifferences()){
                if(isEmpty(getTargetDate().getNotes()) && !isEmpty(getOtherDate().getNotes())){
                    getTargetDate().setNotes(getOtherDate().getNotes());
                }
            }
        }

        private RelativeDate getOtherDate(){
            if(useFromAsDefault && container.getToMapping() != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY){
                return container.getToMapping().getSeadData().get(0);
            } else if(!useFromAsDefault && container.getFromMapping() != UncertaintyExtractor.NO_DATES_CALENDAR_FOR_UNCERTAINTY){
                return container.getFromMapping().getSeadData().get(0);
            } else {
                return new RelativeDate();
            }
        }

        private static final boolean isEmpty(String value){
            return value == null || value.trim().isEmpty();
        }

    }
}
