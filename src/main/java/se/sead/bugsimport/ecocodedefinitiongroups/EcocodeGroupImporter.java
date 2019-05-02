package se.sead.bugsimport.ecocodedefinitiongroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;

@Service
public class EcocodeGroupImporter extends Importer<EcoDefGroups, EcocodeGroup> {

    @Autowired
    public EcocodeGroupImporter(
            BugsSeadMapper<EcoDefGroups, EcocodeGroup> dataMapper,
            EcocodeGroupPersister persister) {
        super(dataMapper, persister);
    }
}
