package se.sead.bugsimport.countsheets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.site.SiteImporter;

@Service
public class SampleGroupImporter extends Importer<Countsheet, SampleGroup> {

    @Autowired
    public SampleGroupImporter(
            SampleGroupBugsSeadMapper dataMapper,
            SampleGroupPersister persister,
            SiteImporter siteImporter) {
        super(dataMapper, persister, siteImporter);
    }
}
