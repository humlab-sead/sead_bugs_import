package se.sead.bugsimport.attributes.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.BigDecimalDefinition;
import se.sead.utils.errorlog.ErrorLog;
import se.sead.utils.errorlog.IgnoredItemErrorLog;
import se.sead.utils.errorlog.SingleMessageErrorLog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TaxaMeasuredAttributesUpdater {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;

    public void update(TaxaMeasuredAttributes original, BugsAttributes bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private TaxaMeasuredAttributes original;
        private BugsAttributes bugsData;
        private boolean updated = false;
        private MeasuredAttributeErrorLog errorLog = new MeasuredAttributeErrorLog();

        Updater(TaxaMeasuredAttributes original, BugsAttributes bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            updated = setType();
            updated = setMeasure() || updated;
            updated = setValue() || updated;
            updated = setUnits() || updated;
            updated = setSpecies() || updated;
            if(errorLog.hasErrors()){
                original.addError(errorLog.getErrorLog());
            }
            original.setUpdated(updated);
        }

        private boolean setType() {
            String originalType = original.getType();
            String attribType = bugsData.getType();
            if(attribType == null || attribType.isEmpty()){
                errorLog.addError(MeasuredAttributeErrorLog.NO_TYPE);
                return false;
            }
            original.setType(attribType);
            return !Objects.equals(originalType, attribType);
        }

        private boolean setMeasure() {
            String originalMeasure = original.getMeasure();
            original.setMeasure(bugsData.getMeasure());
            return !Objects.equals(originalMeasure, bugsData.getMeasure());
        }

        private boolean setValue() {
            BigDecimal originalValue = original.getValue();
            BigDecimal newValue = BigDecimalDefinition.convertToSeadContext(bugsData.getValue());
            if(newValue == null){
                errorLog.addError(MeasuredAttributeErrorLog.NO_VALUE);
                return false;
            }
            original.setValue(newValue);
            return !BigDecimalDefinition.equalBigDecimalNumericValues(originalValue, newValue);
        }

        private boolean setUnits() {
            String originalUnits = original.getUnits();
            String attribUnits = bugsData.getUnits();
            if(attribUnits == null || attribUnits.isEmpty()){
                errorLog.addError(MeasuredAttributeErrorLog.NO_UNIT);
                return false;
            }
            original.setUnits(attribUnits);
            return !Objects.equals(originalUnits, attribUnits);
        }

        private boolean setSpecies() {
            TaxaSpecies originalSpecies = original.getSpecies();
            TaxaSpecies bugsSpeciesByCode = taxonomicOrderRepository.findBugsSpeciesByCode(bugsData.getCode());
            original.setSpecies(bugsSpeciesByCode);
            if(bugsSpeciesByCode == null){
                errorLog.addError(MeasuredAttributeErrorLog.NO_SPECIES);
            }
            return !Objects.equals(originalSpecies, bugsSpeciesByCode);
        }
    }

    private static class MeasuredAttributeErrorLog {

        static String NO_VALUE = "No attribute value";
        static String NO_UNIT = "No attribute unit";
        static String NO_TYPE = "No attribute type";
        static String NO_SPECIES = "No species found for code";


        private List<String> errorLogs = new ArrayList<>();

        void addError(String errorMessage){
            errorLogs.add(errorMessage);
        }

        ErrorLog getErrorLog() {
            if(errorLogs.isEmpty()){
                return null;
            } else if(logsContainAllIgnoreMarkers()){
                return new IgnoredItemErrorLog(compressErrors());
            }
            return new SingleMessageErrorLog(compressErrors());
        }

        private boolean logsContainAllIgnoreMarkers(){
            return (
                    errorLogs.size() == 3 &&
                    errorLogs.contains(NO_VALUE) &&
                    errorLogs.contains(NO_TYPE) &&
                    errorLogs.contains(NO_UNIT)
                    )
                    ||
                    (
                    errorLogs.size() == 2 &&
                    errorLogs.contains(NO_TYPE) &&
                    errorLogs.contains(NO_UNIT)
                    )
                    ;
        }

        private String compressErrors(){
            return String.join(
                    ", ",
                    errorLogs
            );
        }

        boolean hasErrors(){
            return !errorLogs.isEmpty();
        }
    }
}
