package se.sead.bugsimport.ecocodes.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.ecocodedefinition.koch.KochDefinitionRowConverter;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.bugsimport.ecocodes.EcocodeCreator;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;

@Component
public class KochEcocodesCreator {

    @Autowired
    private KochDefinitionRowConverter.KochDefinitionTraceHelper definitionTraceHelper;
    @Autowired
    private EcocodeCreator ecocodeCreator;

    public Ecocode create(EcoKoch bugsData){
        return ecocodeCreator.create(new KochEcocodeDataProvider(bugsData));
    }

    private class KochEcocodeDataProvider implements EcocodeCreator.EcocodeDataProvider {

        private EcoKoch bugsData;

        KochEcocodeDataProvider(EcoKoch bugsData){
            this.bugsData = bugsData;
        }

        @Override
        public Double getSpeciesCode() {
            return bugsData.getCode();
        }

        @Override
        public String getEcocodeDefinitionCode() {
            return bugsData.getBugsKochCode();
        }

        @Override
        public EcocodeDefinition getEcocodeDefinition(String code) {
            return definitionTraceHelper.getFromLastTrace(code);
        }

        @Override
        public String getEmptyDefinitionMessage() {
            return "No koch code specified";
        }

        @Override
        public String getNoDefinitionFoundMessage() {
            return "No koch code found";
        }
    }
}
