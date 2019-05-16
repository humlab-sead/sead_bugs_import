package se.sead.bugsimport.periods.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.age.AgeUpdater;
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

    private GeographicExtentUpdater geographicExtentUpdater;
    private RelativeAgeTypeRepository relativeAgeTypeRepository;
    private C14AgeConverter c14AgeConverter;
    private CalendarAgeConverter calendarAgeConverter;

    @Autowired
    public RelativeAgeUpdater(
            GeographicExtentUpdater geographicExtentUpdater,
            RelativeAgeTypeRepository relativeAgeTypeRepository,
            C14AgeConverter c14AgeConverter,
            CalendarAgeConverter calendarAgeConverter){
        this.geographicExtentUpdater = geographicExtentUpdater;
        this.relativeAgeTypeRepository = relativeAgeTypeRepository;
        this.c14AgeConverter = c14AgeConverter;
        this.calendarAgeConverter = calendarAgeConverter;
    }

    public void update(RelativeAge original, Period bugsData){
        new Updater(original, bugsData).update();
    }


    private class Updater {
        private RelativeAge original;
        private Period bugsData;
        private AgeUpdater activeAgeUpdater;

        Updater(RelativeAge original, Period bugsData) {
            this.original = original;
            this.bugsData = bugsData;
            activeAgeUpdater = AgeUpdater.getUpdater(original, bugsData);
        }

        void update(){
            boolean updated = setName();
            updated = setDescription() || updated;
            updated = setBeginAndEnd() || updated;
            updated = setGeographicExtent() || updated;
            updated = setType() || updated;
            updated = setAbbreviation() || updated;

            if (original.getName().isEmpty()) {
                original.setName(original.getAbbreviation());
            }

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

        private boolean setBeginAndEnd(){
            boolean startUpdated = activeAgeUpdater.setBeginDate();
            return activeAgeUpdater.setEndDate() || startUpdated;
        }

        private boolean setGeographicExtent() {
            Location originalGeographicExtent = original.getGeographicExtent();
            Location location = geographicExtentUpdater.getLocation(bugsData);
            if(!location.isErrorFree()){
                ErrorCopier.copyPotentialErrors(original, location);
                return false;
            }
            if(location == GeographicExtentUpdater.NO_OP_LOCATION){
                location = null;
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
