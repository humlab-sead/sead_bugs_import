package se.sead.bugsimport.mcrnames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.bugsimport.species.IndexImporter;

@Service
public class McrNamesImporter extends Importer<BugsMCRNames, MCRName>{

    @Autowired
    public McrNamesImporter(
        McrNamesBugsMapper dataMapper,
        McrNamesPersister persister,
        IndexImporter indexImporter
    ){
        super(dataMapper, persister, indexImporter);
    }
}
