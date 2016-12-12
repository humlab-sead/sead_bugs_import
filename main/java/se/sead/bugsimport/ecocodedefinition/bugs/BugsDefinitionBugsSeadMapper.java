package se.sead.bugsimport.ecocodedefinition.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugsBugsTable;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;

@Component
public class BugsDefinitionBugsSeadMapper extends BugsSeadMapper<EcoDefBugs, EcocodeDefinition> {

    @Autowired
    public BugsDefinitionBugsSeadMapper(BugsDefinitionRowConverter singleBugsTableRowConverterForMapper) {
        super(new EcoDefBugsBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
