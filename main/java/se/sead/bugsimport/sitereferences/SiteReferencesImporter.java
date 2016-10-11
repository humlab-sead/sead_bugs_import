package se.sead.bugsimport.sitereferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.site.SiteImporter;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;

@Service
public class SiteReferencesImporter extends Importer<BugsSiteRef, SiteReference> {

    @Autowired
    public SiteReferencesImporter(
            SiteReferenceBugsSeadMapper dataMapper,
            SiteReferencePersister persister,
            SiteImporter siteImporter) {
        super(dataMapper, persister, siteImporter);
    }
}
