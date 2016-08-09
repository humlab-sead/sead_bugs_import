package se.sead.bugsimport.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;

@Service
public class SiteImporter extends Importer<BugsSite, SeadSite> {

    @Autowired
    public SiteImporter(
            SiteBugsMapper dataMapper,
            SitePersister persister) {
        super(dataMapper, persister);
    }
}
