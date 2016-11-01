package se.sead.bugsimport.ecocodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.BigDecimalDefinition;

@Component
public class EcocodeCreator {

    @Autowired
    private TaxonomicOrderRepository taxonomicOrderRepository;

    public interface EcocodeDataProvider {
        Double getSpeciesCode();
        String getEcocodeDefinitionCode();
        EcocodeDefinition getEcocodeDefinition(String code);
        String getEmptyDefinitionMessage();
        String getNoDefinitionFoundMessage();
    }

    public Ecocode create(EcocodeDataProvider dataProvider){
        return new Creator(dataProvider).create();
    }

    private class Creator {

        private Ecocode seadData;
        private EcocodeDataProvider dataProvider;

        Creator(EcocodeDataProvider dataProvider){
            seadData = new Ecocode();
            this.dataProvider = dataProvider;
        }

        Ecocode create() {
            setDefinition();
            setSpecies();
            return seadData;
        }

        private void setDefinition(){
            String ecocodeDefinitionCode = dataProvider.getEcocodeDefinitionCode();
            if(ecocodeDefinitionCode == null || ecocodeDefinitionCode.isEmpty()){
                seadData.addError(dataProvider.getEmptyDefinitionMessage());
                return;
            }
            EcocodeDefinition definitionFromCode = dataProvider.getEcocodeDefinition(ecocodeDefinitionCode);
            if(definitionFromCode == null){
                seadData.addError(dataProvider.getNoDefinitionFoundMessage());
                return;
            }
            seadData.setEcocodeDefinition(definitionFromCode);
        }

        private void setSpecies(){
            TaxaSpecies speciesFromCODE = taxonomicOrderRepository.findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(getSpeciesCode()));
            if(speciesFromCODE == null){
                seadData.addError("No species found for code");
                return;
            }
            seadData.setSpecies(speciesFromCODE);
        }

        private Double getSpeciesCode() {
            return dataProvider.getSpeciesCode();
        }
    }


}
