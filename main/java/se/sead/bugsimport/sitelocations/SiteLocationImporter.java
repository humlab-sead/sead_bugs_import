package se.sead.bugsimport.sitelocations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.site.SiteImporter;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

@Service
public class SiteLocationImporter extends Importer<BugsSiteLocation, SiteLocation> {

    @Autowired
    public SiteLocationImporter(
            SiteLocationBugsMapper dataMapper,
            SiteLocationPersister persister,
            SiteImporter siteImporter) {
        super(dataMapper, persister, siteImporter);
    }
}
