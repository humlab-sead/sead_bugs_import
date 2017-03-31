package se.sead.bugsimport.datescalendar.cache;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelativeDateMerger {

    private UncertaintyDatesCalendarContainerManager containerManager;
    private RelativeRangeAgeManager ageManager;

    public RelativeDateMerger(
            UncertaintyDatesCalendarContainerManager containerManager,
            RelativeRangeAgeManager ageManager) {
        this.containerManager = containerManager;
        this.ageManager = ageManager;
    }

    public List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mergeItems(
            List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mergeableItems
    ) {
        List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> mergedItems = new ArrayList<>();
        try {
            List<UncertaintyDatesCalendarContainer> ranges = containerManager.createRanges(mergeableItems);
            for (UncertaintyDatesCalendarContainer container :
                    ranges) {
                RelativeAge mergeRelativeAge = ageManager.createOrGet(container.getFromDate(), container.getToDate());
                mergedItems.add(updateAndExtractMapping(container, mergeRelativeAge));
            }
        } catch (UncertaintyExtractor.TooManyUncertaintiesOfSameKindException ex){
            mergedItems.add(new ErrorCreator(ex.getFoundMappingsWithSameKindOfUncertainty()).create());
        }
        return mergedItems;
    }

    private MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> updateAndExtractMapping(
            UncertaintyDatesCalendarContainer container,
            RelativeAge mergeRelativeAge
    ) {
        InformationCopier copier = new InformationCopier(containerManager, container, mergeRelativeAge);
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

    private static class ErrorCreator {
        private List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> problematicItems;

        ErrorCreator(List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> problematicItems){
            this.problematicItems = problematicItems;
        }

        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> create(){
            return
                    new MappingResult.BugsListSeadMapping<>(
                            getCompressedDatesCalendar(),
                            getAllSeadData()
                    );
        }

        private DatesCalendar getCompressedDatesCalendar(){
            List<DatesCalendar> collect = problematicItems.stream()
                    .map(MappingResult.BugsListSeadMapping::getBugsData)
                    .distinct()
                    .collect(Collectors.toList());
            return collect.get(0);
        }

        private List<RelativeDate> getAllSeadData(){
            List<RelativeDate> allSeadData = problematicItems.stream()
                    .map(MappingResult.BugsListSeadMapping::getSeadData)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            allSeadData.forEach(seadValue -> seadValue.addError("Too many uncertainties of same type for a single sample."));
            return allSeadData;
        }
    }
}
