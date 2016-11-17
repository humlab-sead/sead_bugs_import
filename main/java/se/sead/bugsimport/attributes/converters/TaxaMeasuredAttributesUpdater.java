package se.sead.bugsimport.attributes.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.BigDecimalDefinition;

import java.math.BigDecimal;
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
            original.setUpdated(updated);
        }

        private boolean setType() {
            String originalType = original.getType();
            String attribType = bugsData.getType();
            if(attribType == null || attribType.isEmpty()){
                original.addError("No attribute type");
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
                original.addError("No attribute value");
                return false;
            }
            original.setValue(newValue);
            return !BigDecimalDefinition.equalBigDecimalNumericValues(originalValue, newValue);
        }

        private boolean setUnits() {
            String originalUnits = original.getUnits();
            String attribUnits = bugsData.getUnits();
            if(attribUnits == null || attribUnits.isEmpty()){
                original.addError("No attribute unit");
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
                original.addError("No species found for code");
            }
            return !Objects.equals(originalSpecies, bugsSpeciesByCode);
        }
    }
}
