package se.sead.bugsimport.periods.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.age.C14AgeConverter;
import se.sead.bugsimport.periods.converters.age.CalendarAgeConverter;
import se.sead.bugsimport.periods.converters.geography.GeographicExtentUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;
import se.sead.repositories.RelativeAgeTypeRepository;
import se.sead.utils.BigDecimalDefinition;
import se.sead.utils.ErrorCopier;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class RelativeAgeUpdater {

    @Autowired
    private GeographicExtentUpdater geographicExtentUpdater;
    @Autowired
    private RelativeAgeTypeRepository relativeAgeTypeRepository;
    @Autowired
    private C14AgeConverter c14AgeConverter;
    @Autowired
    private CalendarAgeConverter calendarAgeConverter;

    public void update(RelativeAge original, Period bugsData){new Updater(original, bugsData).update();
    }

    private class Updater {
        private RelativeAge original;
        private Period bugsData;

        Updater(RelativeAge original, Period bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            boolean updated = setName();
            updated = setDescription() || updated;
            updated = setBeginAge() || updated;
            updated = setEndAge() || updated;
            updated = setGeographicExtent() || updated;
            updated = setType() || updated;
            updated = setAbbreviation() || updated;
            original.setUpdated(updated);
        }

        private boolean setName() {
            String originalName = original.getName();
            String bugsDataName = bugsData.getName();
            if(bugsDataName == null || bugsDataName.isEmpty()){
                original.addError("No period name given");
            }
            original.setName(bugsDataName);
            return !Objects.equals(originalName, bugsDataName);
        }

        private boolean setDescription() {
            String originalDescription = original.getDescription();
            original.setDescription(bugsData.getDesc());
            return !Objects.equals(originalDescription, bugsData.getDesc());
        }

        private boolean setBeginAge() {
            BigDecimal originalC14AgeOlder = original.getC14AgeOlder();
            BigDecimal originalCalAgeOlder = original.getCalAgeOlder();
            BigDecimal c14BugsAge = c14AgeConverter.getBeginAge(bugsData);
            BigDecimal calendarBugsAge = calendarAgeConverter.getBeginAge(bugsData);
            original.setC14AgeOlder(c14BugsAge);
            original.setCalAgeOlder(calendarBugsAge);
            return !BigDecimalDefinition.equalBigDecimalNumericValues(originalC14AgeOlder, c14BugsAge)
                    || !BigDecimalDefinition.equalBigDecimalNumericValues(originalCalAgeOlder, calendarBugsAge);
        }

        private boolean setEndAge() {
            BigDecimal originalC14AgeYounger = original.getC14AgeYounger();
            BigDecimal originalCalAgeYounger = original.getCalAgeYounger();
            BigDecimal c14AgeYounger = c14AgeConverter.getEndAge(bugsData);
            BigDecimal calendarAgeYounger = calendarAgeConverter.getEndAge(bugsData);
            original.setC14AgeYounger(c14AgeYounger);
            original.setCalAgeYounger(calendarAgeYounger);
            return !BigDecimalDefinition.equalBigDecimalNumericValues(originalC14AgeYounger, c14AgeYounger)
                    || !BigDecimalDefinition.equalBigDecimalNumericValues(originalCalAgeYounger, calendarAgeYounger);
        }

        private boolean setGeographicExtent() {
            Location originalGeographicExtent = original.getGeographicExtent();
            Location location = geographicExtentUpdater.getLocation(bugsData);
            if(!location.isErrorFree()){
                ErrorCopier.copyPotentialErrors(original, location);
                return false;
            }
            original.setGeographicExtent(location);
            return !Objects.equals(originalGeographicExtent, location);
        }

        private boolean setType() {
            RelativeAgeType originalType = original.getType();
            RelativeAgeType bugsType = relativeAgeTypeRepository.findByType(bugsData.getType());
            if(bugsType == null){
                original.addError("No type found");
                return false;
            }
            original.setType(bugsType);
            return !Objects.equals(originalType, bugsType);
        }

        private boolean setAbbreviation() {
            String originalAbbreviation = original.getAbbreviation();
            original.setAbbreviation(bugsData.getPeriodCode());
            return !Objects.equals(originalAbbreviation, bugsData.getPeriodCode());
        }
    }
}
