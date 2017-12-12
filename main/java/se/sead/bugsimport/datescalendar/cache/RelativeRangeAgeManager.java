package se.sead.bugsimport.datescalendar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.converters.AbstractPeriodForRelativeAgeCreator;
import se.sead.bugsimport.datescalendar.converters.RelativeAgeTypeManager;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.RelativeAgeCache;
import se.sead.bugsimport.periods.converters.RelativeAgeUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.repositories.RelativeAgeRepository;

import java.util.Objects;

@Component
public class RelativeRangeAgeManager {

    private RelativeAgeRepository relativeAgeRepository;
    private RelativeAgeUpdater updater;
    private RelativeAgeTypeManager typeManager;
    private RelativeAgeCache relativeAgeCache;
    private AbbreviationCreator abbreviationCreator;

    @Autowired
    public RelativeRangeAgeManager(
            RelativeAgeRepository relativeAgeRepository,
            RelativeAgeUpdater updater,
            RelativeAgeTypeManager typeManager,
            RelativeAgeCache relativeAgeCache
    ){
        this.relativeAgeRepository = relativeAgeRepository;
        this.updater = updater;
        this.typeManager = typeManager;
        this.relativeAgeCache = relativeAgeCache;
        abbreviationCreator = new AbbreviationCreator();
    }

    public RelativeAge createOrGet(DatesCalendar fromData, DatesCalendar toData){
        String abbreviation = createAbbreviation(fromData.getDate(), fromData.getBcadbp(), toData.getDate(), toData.getBcadbp());
        RelativeAge relativeAge = relativeAgeCache.get(abbreviation);
        if(relativeAge != null){
            return relativeAge;
        }
        relativeAge = relativeAgeRepository.findByAbbreviation(abbreviation);
        if(relativeAge == null){
            relativeAge = create(fromData, abbreviation, toData);
        }
        relativeAgeCache.put(relativeAge);
        return relativeAge;
    }

    private String createAbbreviation(Integer start, String fromBcAdBp, Integer stop, String toBcAdBp){
        return abbreviationCreator.createAbbreviation(start, fromBcAdBp, stop, toBcAdBp);
    }

    private RelativeAge create(DatesCalendar from, String abbreviation, DatesCalendar to){
        Period period = new RelativeRangePeriodCreator(from, abbreviation, to).create();
        RelativeAge range = new RelativeAge();
        updater.update(range, period);
        return range;
    }

    private class RelativeRangePeriodCreator extends AbstractPeriodForRelativeAgeCreator {

        private DatesCalendar from;
        private DatesCalendar to;

        public RelativeRangePeriodCreator(DatesCalendar from, String computedAbbreviation, DatesCalendar to) {
            super(from, computedAbbreviation);
            this.from = from;
            this.to = to;
        }

        @Override
        protected void setAgePeriodData(Period period) {
            period.setBegin(from.getDate());
            period.setBeginBCad(from.getBcadbp());
            period.setEnd(to.getDate());
            period.setEndBCad(to.getBcadbp());
        }

        @Override
        protected String getType() {
            return typeManager.getCalendarRangeTypeName();
        }

        @Override
        protected String getYearsType() {
            return "Calendar";
        }
    }

    private static class AbbreviationCreator {

        private static final String SINGLE_NAME_TEMPLATE = "CAL_%d-%d_%s";
        private static final String MULTIPLE_TEMPLATE = "CAL_%d_%s-%d_%s";
        private static final String OPEN_ENDED_START_TEMPLATE = "CAL_-%d_%s";
        private static final String OPEN_ENDED_STOP_TEMPLATE = "CAL_%d_%s-";

        String createAbbreviation(Integer start, String fromBcAdBp, Integer stop, String toBcAdBp){
            if(start == null){
                return String.format(
                        OPEN_ENDED_START_TEMPLATE,
                        stop,
                        toBcAdBp
                );
            } else if(stop == null){
                return String.format(
                        OPEN_ENDED_STOP_TEMPLATE,
                        start,
                        fromBcAdBp
                );
            }
            String bcAdBp = getBcAdBpFromOne(fromBcAdBp, toBcAdBp);
            if(bcAdBp != null){
                return String.format(
                        SINGLE_NAME_TEMPLATE,
                        start,
                        stop,
                        bcAdBp);
            } else {
                return String.format(
                        MULTIPLE_TEMPLATE,
                        start,
                        fromBcAdBp,
                        stop,
                        toBcAdBp
                );
            }
        }

        private String getBcAdBpFromOne(String from, String to){
            if(isEmpty(from) && !isEmpty(to)){
                return to;
            } else if(!isEmpty(from) && isEmpty(to)){
                return from;
            } else if(sameBcAdBp(from, to)){
                return from;
            } else {
                return null;
            }
        }

        private boolean sameBcAdBp(String fromVariant, String toVariant){
            return Objects.equals(fromVariant, toVariant);
        }

        private boolean isEmpty(String bcadbp){
            return bcadbp == null || bcadbp.trim().isEmpty();
        }
    }
}
