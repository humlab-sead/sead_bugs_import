package se.sead.bugsimport.sitelocations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.repositories.SiteLocationRepository;

@Component
public class SiteLocationPersister extends Persister<BugsSiteLocation, SiteLocation>{

    @Autowired
    private SiteLocationRepository siteLocationRepository;

    @Override
    protected SiteLocation save(SiteLocation seadValue) {
        return siteLocationRepository.saveOrUpdate(seadValue);
    }
}
