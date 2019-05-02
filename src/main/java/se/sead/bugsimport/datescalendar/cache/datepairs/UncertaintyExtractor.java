package se.sead.bugsimport.datescalendar.cache.datepairs;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.cache.DatingUncertaintyManager;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class UncertaintyExtractor {


    protected DatingUncertaintyManager uncertaintyManager;

    protected UncertaintyExtractor(DatingUncertaintyManager manager){
        this.uncertaintyManager = manager;
    }

    BugsListSeadMapping<DatesCalendar, RelativeDate> getForUncertainty(
            List<BugsListSeadMapping<DatesCalendar, RelativeDate>> carriers)
            throws TooManyUncertaintiesOfSameKindException {
        List<BugsListSeadMapping<DatesCalendar, RelativeDate>> foundDates = new ArrayList<>();
        for (BugsListSeadMapping<DatesCalendar, RelativeDate> carrier:
                carriers){
            RelativeDate relativeDate = carrier.getSeadData().get(0);
            if(isUncertainty(relativeDate)){
                foundDates.add(carrier);
            }
        }
        if(foundDates.isEmpty()){
            return DatesCalendarMappingContainer.NO_DATES_CALENDAR_FOR_UNCERTAINTY;
        } else if(foundDates.size() > 1){
            throw new TooManyUncertaintiesOfSameKindException(foundDates);
        }
        return foundDates.get(0);
    }

    protected abstract boolean isUncertainty(RelativeDate relativeDate);

    static class FromUncertaintyExtractor extends UncertaintyExtractor{

        FromUncertaintyExtractor(DatingUncertaintyManager uncertaintyManager) {
            super(uncertaintyManager);
        }

        @Override
        protected boolean isUncertainty(RelativeDate relativeDate) {
            return uncertaintyManager.isFromUncertainty(relativeDate.getUncertainty());
        }
    }

    static class FromCaUncertaintyExtractor extends UncertaintyExtractor {

        FromCaUncertaintyExtractor(DatingUncertaintyManager manager) {
            super(manager);
        }

        @Override
        protected boolean isUncertainty(RelativeDate relativeDate) {
            return uncertaintyManager.isFromCaUncertainty(relativeDate.getUncertainty());
        }
    }

    static class ToUncertaintyExtractor extends UncertaintyExtractor {
        ToUncertaintyExtractor(DatingUncertaintyManager manager) {
            super(manager);
        }

        @Override
        protected boolean isUncertainty(RelativeDate relativeDate) {
            return uncertaintyManager.isToUncertainty(relativeDate.getUncertainty());
        }
    }

    static class ToCaUncertaintyExtractor extends UncertaintyExtractor {
        ToCaUncertaintyExtractor(DatingUncertaintyManager manager) {
            super(manager);
        }

        @Override
        protected boolean isUncertainty(RelativeDate relativeDate) {
            return uncertaintyManager.isToCaUncertainty(relativeDate.getUncertainty());
        }
    }

}
