package se.sead.bugsimport.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.site.seadmodel.SiteLocation;
import se.sead.repositories.SiteLocationRepository;
import se.sead.repositories.SiteRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class SitePersister extends Persister<BugsSite, SeadSite> {

    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteLocationRepository siteLocationRepository;

    @Override
    protected SeadSite save(SeadSite seadValue) {
        SeadSite saved = siteRepository.saveOrUpdate(seadValue);
//        List<SiteLocation> savedLocations = saveSiteLocations(seadValue.getSiteLocations(), saved);
//        saved.setSiteLocations(savedLocations);
        return saved;
    }

    private List<SiteLocation> saveSiteLocations(List<SiteLocation> suppliedLocations, SeadSite saved) {
        List<SiteLocation> savedLocations = new ArrayList<>();
        for (SiteLocation siteLocation:
                suppliedLocations){
            siteLocation.setSite(saved);
            siteLocation = siteLocationRepository.saveOrUpdate(siteLocation);
            if(siteLocation != null) {
                savedLocations.add(siteLocation);
            }
        }
        return savedLocations;
    }
}
