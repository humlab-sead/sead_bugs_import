package se.sead.bugsimport.ecocodes.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.ecocodedefinition.bugs.BugsDefinitionRowConverter;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodes.EcocodeCreator;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;

@Component
public class BugsEcocodeCreator {

    @Autowired
    private BugsDefinitionRowConverter.BugsDefinitionTraceHelper definitionTraceHelper;

    @Autowired
    private EcocodeCreator ecocodeCreator;

    public Ecocode create(EcoBugs bugsData){
        return ecocodeCreator.create(new BugsCreatorDataProvider(bugsData));
    }

    private class BugsCreatorDataProvider implements EcocodeCreator.EcocodeDataProvider{

        private EcoBugs bugsData;

        BugsCreatorDataProvider(EcoBugs bugsData) {
            this.bugsData = bugsData;
        }

        @Override
        public Double getSpeciesCode() {
            return bugsData.getCode();
        }

        @Override
        public String getEcocodeDefinitionCode() {
            return bugsData.getBugsEcoCode();
        }

        @Override
        public EcocodeDefinition getEcocodeDefinition(String code) {
            return definitionTraceHelper.getFromLastTrace(code);
        }

        @Override
        public String getEmptyDefinitionMessage() {
            return "No bugs code specified";
        }

        @Override
        public String getNoDefinitionFoundMessage() {
            return "No bugs code found";
        }
    }
}
