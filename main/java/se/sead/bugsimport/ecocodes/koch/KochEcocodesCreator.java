package se.sead.bugsimport.ecocodes.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.ecocodedefinition.koch.KochDefinitionRowConverter;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.BigDecimalDefinition;

@Component
public class KochEcocodesCreator {

    @Autowired
    private KochDefinitionRowConverter.KochDefinitionTraceHelper definitionTraceHelper;
    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;

    public Ecocode create(EcoKoch bugsData){
        return new Creator(bugsData).create();
    }

    private class Creator {
        private Ecocode seadData;
        private EcoKoch bugsData;

        public Creator(EcoKoch bugsData) {
            this.bugsData = bugsData;
            seadData = new Ecocode();
        }

        Ecocode create(){
            setDefinition();
            setSpecies();
            return seadData;
        }

        private void setDefinition(){
            if(bugsData.getBugsKochCode() == null || bugsData.getBugsKochCode().isEmpty()){
                seadData.addError("No definition provided");
                return;
            }
            EcocodeDefinition definitionFromCode = definitionTraceHelper.getFromLastTrace(bugsData.getBugsKochCode());
            if(definitionFromCode == null){
                seadData.addError("No definition found for code");
                return;
            }
            seadData.setEcocodeDefinition(definitionFromCode);
        }

        private void setSpecies(){
            TaxaSpecies speciesFromCODE = taxonomicOrderRepository.findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(bugsData.getCODE()));
            if(speciesFromCODE == null){
                seadData.addError("No species found for code");
                return;
            }
            seadData.setSpecies(speciesFromCODE);
        }
    }
}
