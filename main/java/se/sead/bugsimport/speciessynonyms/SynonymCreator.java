package se.sead.bugsimport.speciessynonyms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.sead.model.Biblio;
import se.sead.utils.BigDecimalDefinition;
import se.sead.utils.ErrorCopier;

@Component
public class SynonymCreator {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;
    @Autowired
    private SynonymSpeciesManager speciesManager;
    @Autowired
    private SynonymAssociationTypeManager typeManager;
    @Autowired
    private BiblioDataRepository referenceRepository;

    public SpeciesAssociation create(Synonym synonym){
        return new Creator(synonym).create();
    }

    private class Creator {
        private SpeciesAssociation seadData;
        private Synonym synonym;

        Creator(Synonym synonym){
            seadData = new SpeciesAssociation();
            this.synonym = synonym;
        }

        SpeciesAssociation create(){
            setTargetSpecies();
            setSourceSpeciesFromSynonym();
            setAssociationType();
            setReference();
            return seadData;
        }

        private void setTargetSpecies(){
            TaxaSpecies targetSpecies = taxonomicOrderRepository.findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(synonym.getCode()));
            if(targetSpecies == null){
                seadData.addError("No species found for code");
                return;
            }
            seadData.setTargetSpecies(targetSpecies);
        }

        private void setSourceSpeciesFromSynonym(){
            if(!seadData.isErrorFree()){
                return;
            }
            TaxaSpecies sourceSpecies = speciesManager.getOrCreate(seadData.getTargetSpecies(), synonym);
            ErrorCopier.copyPotentialErrors(seadData, sourceSpecies);
            seadData.setSourceSpecies(sourceSpecies);
        }

        private void setAssociationType(){
            seadData.setType(typeManager.getSynonymType());
        }

        private void setReference(){
            if(synonym.getReference() != null){
                Biblio reference = referenceRepository.getByBugsReferenceIgnoreCase(synonym.getReference());
                if(reference == null){
                    seadData.addError("No reference found");
                } else {
                    seadData.setReference(reference);
                }
            }
        }
    }
}
