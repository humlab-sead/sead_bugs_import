package se.sead.bugsimport.datescalendar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.util.List;

@Component
public class RelativeAgeMerger {

    @Autowired
    private RelativeRangeAgeManager relativeAgeManager;
    @Autowired
    private DatingUncertaintyManager uncertaintyManager;

    RelativeAge createRange(List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> carriers){
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> fromCarrier = getFromCarrier(carriers);
        MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> toCarrier = getToCarrier(carriers);
        return relativeAgeManager.createOrGet(fromCarrier.getBugsData(), toCarrier.getBugsData());
    }

    private MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> getFromCarrier(List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> carriers){
        for (MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> carrier:
                carriers){
            RelativeDate relativeDate = carrier.getSeadData().get(0);
            if(uncertaintyManager.isFromUncertaintyWithoutCaValidation(relativeDate.getUncertainty())){
                return carrier;
            }
        }
        throw new IllegalStateException("No From type found in list of probable range date calendars");
    }

    private MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> getToCarrier(List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> carriers){
        for (MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> carrier:
                carriers){
            RelativeDate relativeDate = carrier.getSeadData().get(0);
            if(uncertaintyManager.isToUncertaintyWithoutCaValidation(relativeDate.getUncertainty())){
                return carrier;
            }
        }
        throw new IllegalStateException("No To type found in list of probable range date calendars");
    }

    void updateUncertaintyIfNeeded(RelativeDate originalRelativeDate){
        DatingUncertainty newDatingUncertainty = uncertaintyManager.convertUncertainty(originalRelativeDate.getUncertainty());
        originalRelativeDate.setUncertainty(newDatingUncertainty);
    }
}
