package se.sead.bugsimport.datescalendar.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.RelativeAgeCache;
import se.sead.bugsimport.periods.converters.RelativeAgeUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.RelativeAgeRepository;

@Component
public class RelativeAgeManager {

    private static final String NAME_TEMPLATE = "CAL_%d_%s";

    @Autowired
    private RelativeAgeRepository relativeAgeRepository;
    @Autowired
    private RelativeAgeUpdater updater;
    @Autowired
    private RelativeAgeTypeManager typeManager;
    @Autowired
    private RelativeAgeCache ageCache;

    public RelativeAge getOrCreateRelativeAge(DatesCalendar bugsData){
        String abbreviation = createAbbreviation(bugsData.getDate(), bugsData.getBcadbp());
        RelativeAge relativeAge = ageCache.get(abbreviation);
        if(relativeAge != null){
            return relativeAge;
        }
        relativeAge = relativeAgeRepository.findByAbbreviation(abbreviation);
        if(relativeAge == null){
            relativeAge =  create(bugsData, abbreviation);
        }
        ageCache.put(relativeAge);
        return relativeAge;
    }

    private String createAbbreviation(Integer date, String bcadbp){
        return String.format(NAME_TEMPLATE, date, bcadbp);
    }

    protected RelativeAge create(DatesCalendar bugsData, String abbreviation){
        Period period = new PeriodCreator(bugsData, abbreviation).create();
        RelativeAge created = new RelativeAge();
        updater.update(created, period);
        return created;
    }

    private class PeriodCreator extends AbstractPeriodForRelativeAgeCreator {

        public PeriodCreator(DatesCalendar datesCalendar, String computedAbbreviation) {
            super(datesCalendar, computedAbbreviation);
        }

        @Override
        protected void setAgePeriodData(Period period) {
            period.setBegin(datesCalendar.getDate());
            period.setEnd(datesCalendar.getDate());
            period.setBeginBCad(datesCalendar.getBcadbp());
            period.setEndBCad(datesCalendar.getBcadbp());
        }

        @Override
        protected String getType(){
            return typeManager.getCalendarDateTypeName();
        }
    }

}
