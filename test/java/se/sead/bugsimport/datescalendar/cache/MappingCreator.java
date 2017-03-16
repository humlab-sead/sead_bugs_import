package se.sead.bugsimport.datescalendar.cache;

import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;

import java.util.Arrays;

class MappingCreator {

    MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> createMapping(String sampleCode, String uncertainty){
        DatesCalendar datesCalendar = createDatesCalendar(sampleCode, uncertainty);
        DatingUncertainty datingUncertainty = createDatingUncertainty(uncertainty);
        RelativeDate relativeDate = createRelativeDate(datingUncertainty);
        return new MappingResult.BugsListSeadMapping<>(datesCalendar, Arrays.asList(relativeDate));
    }

    private DatesCalendar createDatesCalendar(String sampleCode, String uncertainty){
        DatesCalendar datesCalendar = new DatesCalendar();
        datesCalendar.setSample(sampleCode);
        datesCalendar.setUncertainty(uncertainty);
        return datesCalendar;
    }

    private DatingUncertainty createDatingUncertainty(String uncertainty){
        DatingUncertainty datingUncertainty = new DatingUncertainty();
        datingUncertainty.setName(uncertainty);
        return datingUncertainty;
    }

    private RelativeDate createRelativeDate(DatingUncertainty datingUncertainty){
        RelativeDate relativeDate = new RelativeDate();
        relativeDate.setUncertainty(datingUncertainty);
        return relativeDate;
    }
}
