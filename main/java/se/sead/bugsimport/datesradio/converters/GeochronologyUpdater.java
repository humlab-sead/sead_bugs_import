package se.sead.bugsimport.datesradio.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.lab.search.DatingLabTraceHelper;
import se.sead.repositories.DatingUncertaintyRepository;
import se.sead.sead.data.AnalysisEntity;
import se.sead.utils.BigDecimalDefinition;
import se.sead.utils.ErrorCopier;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

@Component
public class GeochronologyUpdater {

    private static final MathContext SEAD_AGE_CONTEXT = new MathContext(20, RoundingMode.HALF_DOWN);
    private static final int SEAD_AGE_SCALE = 5;

    @Autowired
    private GeochronologyAnalysisEntityCreator analysisEntityCreator;
    @Autowired
    private DatingLabTraceHelper datingLabTraceHelper;
    @Autowired
    private DatingUncertaintyRepository datingUncertaintyRepository;

    public void update(Geochronology original, DatesRadio bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private Geochronology original;
        private DatesRadio bugsData;

        Updater(Geochronology original, DatesRadio bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            boolean updated = setAnalysisEntity();
            updated = setDatingLab() || updated;
            updated = setLabSampleNumber() || updated;
            updated = setAge() || updated;
            updated = setErrorOlder() || updated;
            updated = setErrorYounger() || updated;
            updated = setNotes() || updated;
            updated = setUncertainty() || updated;
            original.setUpdated(updated);
        }

        private boolean setAnalysisEntity() {
            AnalysisEntity originalAnalysisEntity = original.getAnalysisEntity();
            if(originalAnalysisEntity != null){
                return false;
            }
            AnalysisEntity newEntity = analysisEntityCreator.create(bugsData);
            original.setAnalysisEntity(newEntity);
            ErrorCopier.copyPotentialErrors(original, newEntity);
            return true;
        }

        private boolean setDatingLab() {
            DatingLab originalDatingLaboratory = original.getDatingLaboratory();
            DatingLab fromLastTrace = datingLabTraceHelper.getFromLastTrace(bugsData.getLabId());
            if(fromLastTrace == null){

                // must be ok, but only if field is empty in bugs
                original.addError("No lab found");
            }
            original.setDatingLaboratory(fromLastTrace);
            return !Objects.equals(originalDatingLaboratory, fromLastTrace);
        }

        private boolean setLabSampleNumber() {
            String originalLabSampleNumber = original.getLabSampleNumber();
            original.setLabSampleNumber(bugsData.getLabNr());
            return !Objects.equals(originalLabSampleNumber, bugsData.getLabNr());
        }

        private boolean setAge() {
            BigDecimal originalAge = original.getAge();
            if(bugsData.getDate() == null){
                original.addError("No date found");
                return false;
            } else {
                BigDecimal newAge = createSeadValue(bugsData.getDate());
                original.setAge(newAge);
                return BigDecimalDefinition.equalBigDecimalNumericValues(originalAge, newAge);
            }
        }

        private BigDecimal createSeadValue(Integer numericData){
            BigDecimal value = new BigDecimal(numericData, SEAD_AGE_CONTEXT);
            value.setScale(SEAD_AGE_SCALE);
            return value;
        }

        private boolean setErrorOlder() {
            BigDecimal originalErrorOlder = original.getErrorOlder();
            BigDecimal bugsError = null;
            if(bugsData.getAgeErrorOrPlusError() != null){
                bugsError = createSeadValue(bugsData.getAgeErrorOrPlusError().intValue());
            }
            original.setErrorOlder(bugsError);
            return !BigDecimalDefinition.equalBigDecimalNumericValues(originalErrorOlder, bugsError);
        }

        private boolean setErrorYounger() {
            BigDecimal originalErrorYounger = original.getErrorYounger();
            BigDecimal bugsError = null;
            if(bugsData.getAgeErrorMinus() != null){
                bugsError = createSeadValue(bugsData.getAgeErrorMinus());
            } else if(bugsData.getAgeErrorOrPlusError() != null){
                bugsError = createSeadValue(bugsData.getAgeErrorOrPlusError().intValue());
            }
            original.setErrorYounger(bugsError);
            return !BigDecimalDefinition.equalBigDecimalNumericValues(originalErrorYounger, bugsError);
        }

        private boolean setNotes() {
            String originalNotes = original.getNotes();
            String notes = bugsData.getNotes();
            original.setNotes(notes);
            return !Objects.equals(originalNotes, notes);
        }

        private boolean setUncertainty() {
            DatingUncertainty originalUncertainty = original.getUncertainty();
            DatingUncertainty uncertainty = datingUncertaintyRepository.findByName(bugsData.getUncertainty());
            if(bugsDataContainUncertaintyButMatchIsNotFoundInSead(uncertainty)){
                original.addError("Unknown uncertainty symbol");
                return false;
            }
            original.setUncertainty(uncertainty);
            return !Objects.equals(originalUncertainty, uncertainty);
        }

        private boolean bugsDataContainUncertaintyButMatchIsNotFoundInSead(DatingUncertainty uncertainty) {
            return bugsData.getUncertainty() != null && !bugsData.getUncertainty().trim().isEmpty() && uncertainty == null;
        }

    }
}
