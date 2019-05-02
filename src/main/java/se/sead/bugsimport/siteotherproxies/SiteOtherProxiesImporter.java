package se.sead.bugsimport.siteotherproxies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.site.SiteImporter;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;

@Service
public class SiteOtherProxiesImporter extends Importer<SiteOtherProxies, SiteOtherRecord> {

    @Autowired
    public SiteOtherProxiesImporter(
            SiteOtherProxiesBugsMapper dataMapper,
            SiteOtherProxiesPersister persister,
            SiteImporter siteImporter) {
        super(dataMapper, persister, siteImporter);
    }
}
