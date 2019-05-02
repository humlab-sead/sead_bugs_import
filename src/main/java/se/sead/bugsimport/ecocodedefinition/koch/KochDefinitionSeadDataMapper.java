package se.sead.bugsimport.ecocodedefinition.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKochBugsTable;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;

@Component
public class KochDefinitionSeadDataMapper extends BugsSeadMapper<EcoDefKoch, EcocodeDefinition>{

    @Autowired
    public KochDefinitionSeadDataMapper(KochDefinitionRowConverter singleBugsTableRowConverterForMapper) {
        super(new EcoDefKochBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
