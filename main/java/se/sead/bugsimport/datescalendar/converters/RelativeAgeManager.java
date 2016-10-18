package se.sead.bugsimport.datescalendar.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.RelativeAgeUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.RelativeAgeRepository;

@Component
public class RelativeAgeManager {

    private static final String NAME_TEMPLATE = "CAL_%d_%s";
    private static final String STANDARD_NOTES = "Autocreated from bugs import";

    @Autowired
    private RelativeAgeRepository relativeAgeRepository;
    @Autowired
    private RelativeAgeUpdater updater;

    public RelativeAge getOrCreateRelativeAge(DatesCalendar bugsData){
        String abbreviation = createAbbreviation(bugsData.getDate(), bugsData.getBcadbp());
        RelativeAge byAbbreviation = relativeAgeRepository.findByAbbreviation(abbreviation);
        if(byAbbreviation == null){
            return create(bugsData, abbreviation);
        } else {
            return byAbbreviation;
        }
    }

    private String createAbbreviation(Integer date, String bcadbp){
        return String.format(NAME_TEMPLATE, date, bcadbp);
    }

    private RelativeAge create(DatesCalendar bugsData, String abbreviation){
        return new RelativeAgeCreator(bugsData, abbreviation).create();
    }

    private class RelativeAgeCreator {

        private DatesCalendar datesCalendar;
        private String computedAbbreviation;

        public RelativeAgeCreator(DatesCalendar datesCalendar, String computedAbbreviation) {
            this.datesCalendar = datesCalendar;
            this.computedAbbreviation = computedAbbreviation;
        }

        RelativeAge create(){
            Period period = new Period();
            period.setBegin(datesCalendar.getDate());
            period.setEnd(datesCalendar.getDate());
            period.setBeginBCad(datesCalendar.getBcadbp());
            period.setEndBCad(datesCalendar.getBcadbp());
            period.setType(datesCalendar.getDatingMethod());
            period.setDesc(STANDARD_NOTES);
            period.setPeriodCode(computedAbbreviation);
            RelativeAge created = new RelativeAge();
            updater.update(created, period);
            return created;
        }
    }

}
