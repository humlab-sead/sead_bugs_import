package se.sead.bugsimport.ecocodedefinitiongroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroupsBugsTable;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;

@Component
public class EcocodeGroupBugsSeadMapper extends BugsSeadMapper<EcoDefGroups, EcocodeGroup> {

    @Autowired
    public EcocodeGroupBugsSeadMapper(BugsTableRowConverter<EcoDefGroups, EcocodeGroup> singleBugsTableRowConverterForMapper) {
        super(new EcoDefGroupsBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
