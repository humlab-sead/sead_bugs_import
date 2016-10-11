package se.sead.bugsimport.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.repositories.SiteRepository;

@Component
public class SitePersister extends Persister<BugsSite, SeadSite> {

    @Autowired
    private SiteRepository siteRepository;

    @Override
    protected SeadSite save(SeadSite seadValue) {
        return siteRepository.saveOrUpdate(seadValue);
    }
}
