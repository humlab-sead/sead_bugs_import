package se.sead.bugsimport.datasetcontacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.datasetcontacts.updater.ContactCacheAndRepositoryAccessor;
import se.sead.bugsimport.fossil.FossilImporter;
import se.sead.sead.data.DatasetContact;

@Service
public class DatasetContactImporter extends Importer<Countsheet, DatasetContact> {

    @Autowired
    private ContactCacheAndRepositoryAccessor contactCacheAndRepositoryAccessor;

    @Autowired
    public DatasetContactImporter(
            DatasetContactBugsSeadMapper dataMapper,
            DatasetContactPersister persister,
            FossilImporter fossilImporter) {
        super(dataMapper, persister, fossilImporter);
    }

    @Override
    public void run() {
        super.run();
        contactCacheAndRepositoryAccessor.clearCache();
    }
}
