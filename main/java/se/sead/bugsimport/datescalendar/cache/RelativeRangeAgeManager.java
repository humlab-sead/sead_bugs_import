package se.sead.bugsimport.datescalendar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.converters.AbstractPeriodForRelativeAgeCreator;
import se.sead.bugsimport.datescalendar.converters.RelativeAgeTypeManager;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.RelativeAgeUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.RelativeAgeRepository;

@Component
public class RelativeRangeAgeManager {

    private static final String NAME_TEMPLATE = "CAL_%d-%d_%s";

    @Autowired
    private RelativeAgeRepository relativeAgeRepository;
    @Autowired
    private RelativeAgeUpdater updater;
    @Autowired
    private RelativeAgeTypeManager typeManager;

    public RelativeAge createOrGet(DatesCalendar fromData, DatesCalendar toData){
        String abbreviation = createAbbreviation(fromData.getDate(), toData.getDate(), fromData.getBcadbp());
        RelativeAge byAbbreviation = relativeAgeRepository.findByAbbreviation(abbreviation);
        if(byAbbreviation == null){
            return create(fromData, abbreviation, fromData.getDate(), toData.getDate());
        } else {
            return byAbbreviation;
        }
    }

    private String createAbbreviation(Integer start, Integer stop, String bcadbp){
        return String.format(
                NAME_TEMPLATE,
                start,
                stop,
                bcadbp);
    }

    private RelativeAge create(DatesCalendar bugsData, String abbreviation, Integer start, Integer stop){
        Period period = new RelativeRangePeriodCreator(bugsData, abbreviation, start, stop).create();
        RelativeAge range = new RelativeAge();
        updater.update(range, period);
        return range;
    }

    private class RelativeRangePeriodCreator extends AbstractPeriodForRelativeAgeCreator {
        private Integer start;
        private Integer stop;

        public RelativeRangePeriodCreator(DatesCalendar datesCalendar, String computedAbbreviation, Integer start, Integer stop) {
            super(datesCalendar, computedAbbreviation);
            this.start = start;
            this.stop = stop;
        }

        @Override
        protected Integer getStart() {
            return start;
        }

        @Override
        protected Integer getStop() {
            return stop;
        }

        @Override
        protected String getType() {
            return typeManager.getCalendarRangeTypeName();
        }
    }
}
