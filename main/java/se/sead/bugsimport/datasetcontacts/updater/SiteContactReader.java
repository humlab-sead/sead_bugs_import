package se.sead.bugsimport.datasetcontacts.updater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessDatabase;
import se.sead.bugs.AccessDatabaseProvider;
import se.sead.bugs.AccessSearcher;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.bugsmodel.BugsSiteBugsTable;

import java.util.Optional;

@Component
public class SiteContactReader {

    private AccessDatabaseProvider accessDatabaseProvider;
    private AccessSearcher<BugsSite> bugsSiteSearcher;

    @Autowired
    public SiteContactReader(AccessDatabaseProvider accessDatabaseProvider) {
        this.accessDatabaseProvider = accessDatabaseProvider;
    }

    public SiteContactStringData parse(String bugsSiteCode){
        initSearcher();
        Optional<BugsSite> foundSite = bugsSiteSearcher.search(createCriteria(bugsSiteCode));
        if(foundSite.isPresent()){
            return parseStringData(foundSite.get());
        } else {
            return new SiteContactStringData();
        }
    }

    private void initSearcher(){
        if(bugsSiteSearcher == null){
            bugsSiteSearcher = new BugsSiteAccessSearcher(accessDatabaseProvider.getDatabase());
        }
    }

    private AccessSearcher.SearchCriteria<String> createCriteria(String bugsSiteCode){
        return new AccessSearcher.SearchCriteria<>("SiteCODE", bugsSiteCode);
    }

    private SiteContactStringData parseStringData(BugsSite site){
        SiteContactStringData siteContactStringData = new SiteContactStringData();
        siteContactStringData.setIdentifiedBy(site.getIDBy());
        siteContactStringData.setSpecimenRepository(site.getSpecimens());
        return siteContactStringData;
    }


    public static class SiteContactStringData {
        private String identifiedBy;
        private String specimenRepository;

        void setIdentifiedBy(String identifiedBy){
            this.identifiedBy = identifiedBy;
        }

        public String getIdentifiedBy() {
            return identifiedBy;
        }

        void setSpecimenRepository(String specimenRepository){
            this.specimenRepository = specimenRepository;
        }

        public String getSpecimenRepository() {
            return specimenRepository;
        }
    }

    private static class BugsSiteAccessSearcher extends AccessSearcher<BugsSite> {

        BugsSiteAccessSearcher(AccessDatabase accessDatabase){
            super(accessDatabase, new BugsSiteBugsTable());
        }
    }
}
