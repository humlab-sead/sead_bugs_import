package se.sead.bugsimport.speciesassociation.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociationType;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.SpeciesAssociationTypeRepository;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;

import java.util.Objects;

import static se.sead.utils.BigDecimalDefinition.convertToSeadCode;

@Component
public class SpeciesAssociationUpdater {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private SpeciesAssociationTypeRepository speciesAssociationTypeRepository;
    @Autowired
    private BiblioDataRepository referenceRepository;

    public void update(SpeciesAssociation original, BugsSpeciesAssociation bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private SpeciesAssociation original;
        private BugsSpeciesAssociation bugsData;

        Updater(SpeciesAssociation original, BugsSpeciesAssociation bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            boolean updated = setSourceSpecies();
            updated = setTargetSpecies() || updated;
            updated = setType() || updated;
            updated = setReference() || updated;
            original.setUpdated(updated);
        }

        private boolean setSourceSpecies(){
            if(bugsData.getCode() == null){
                original.addError("No source association");
                return false;
            }
            TaxaSpecies originalSourceSpecies = original.getSourceSpecies();
            TaxaSpecies bugsSpeciesByCode = taxonomicOrderRepository.findBugsSpeciesByCode(convertToSeadCode(bugsData.getCode()));
            if(bugsSpeciesByCode == null){
                original.addError("Source species not found");
                return false;
            }
            original.setSourceSpecies(bugsSpeciesByCode);
            return !Objects.equals(originalSourceSpecies, bugsSpeciesByCode);
        }

        private boolean setTargetSpecies(){
            if(bugsData.getAssociatedSpeciesCODE() == null){
                original.addError("No target association");
                return false;
            }
            TaxaSpecies originalTargetSpecies = original.getTargetSpecies();
            TaxaSpecies bugsSpeciesByCode = taxonomicOrderRepository.findBugsSpeciesByCode(convertToSeadCode(bugsData.getAssociatedSpeciesCODE()));
            if(bugsSpeciesByCode == null){
                original.addError("Target species not found");
                return false;
            }
            original.setTargetSpecies(bugsSpeciesByCode);
            return !Objects.equals(originalTargetSpecies, bugsSpeciesByCode);
        }

        private boolean setType(){
            SpeciesAssociationType type = findType(bugsData.getAssociationType());
            if(type == null){
                original.addError("Association type not found");
                return false;
            }
            SpeciesAssociationType originalType = original.getType();
            original.setType(type);
            return !Objects.equals(originalType, type);
        }

        private SpeciesAssociationType findType(String bugsAssociationType){
            if(bugsAssociationType == null){
                return speciesAssociationTypeRepository.getDefaultType();
            } else {
                return speciesAssociationTypeRepository.findByName(bugsAssociationType);
            }
        }

        private boolean setReference(){
            Biblio originalReference = original.getReference();
            Biblio bugsReference = null;
            if(bugsData.getRef() != null && !bugsData.getRef().isEmpty()){
                 bugsReference = referenceRepository.getByBugsReferenceIgnoreCase(bugsData.getRef());
                if(bugsReference == null){
                    original.addError("Reference not found");
                    return false;
                }
            }
            original.setReference(bugsReference);
            return !Objects.equals(originalReference, bugsReference);
        }
    }
}
