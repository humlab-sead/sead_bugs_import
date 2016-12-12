package se.sead.bugsimport.site.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.conversion.locations.SiteLocationHandler;
import se.sead.bugsimport.site.helper.SiteFromCodeAllowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.repositories.SiteRepository;

import java.util.List;

@Component
public class BugsSiteTableConverter implements BugsTableRowConverter<BugsSite, SeadSite>{

    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteFromCodeAllowDeletedSite importSiteHelper;
    @Value("${allow.create.country:true}")
    private Boolean canCreateCountry;
    @Value("${allow.site.updates:false}")
    private Boolean allowSiteUpdates;

    @Override
    public SeadSite convertForDataRow(BugsSite bugsData) {
        SiteLocationHandler siteLocationHandler = getLocations(bugsData);
        return getOrCreate(bugsData, siteLocationHandler);
    }

    private SiteLocationHandler getLocations(BugsSite bugsData) {
        return new SiteLocationHandler(locationRepository, locationTypeRepository, bugsData, canCreateCountry);
    }

    private SeadSite getOrCreate(BugsSite bugsSite, SiteLocationHandler siteLocationHandler){
        if(!siteLocationHandler.countryExists()){
            return createMissingCountryLocationSite(bugsSite);
        }
        UpdateHelper updateHelper = new UpdateHelper(bugsSite, siteLocationHandler, importSiteHelper, allowSiteUpdates, siteRepository);
        if(updateHelper.handle()){
            return updateHelper.getFoundSeadSite();
        } else if(updateHelper.getErrorMessage() == null){
            return createSite(bugsSite);
        } else {
            return createErrorSite(bugsSite, updateHelper.getErrorMessage());
        }
    }

    private SeadSite createMissingCountryLocationSite(BugsSite bugsSite){
        return createErrorSite(bugsSite, "No country exists for site");
    }

    private SeadSite createErrorSite(BugsSite bugsSite, String errorMessage){
        return new SeadSiteCreator(
                bugsSite,
                errorMessage
        ).create();
    }

    private SeadSite createSite(BugsSite bugsSite) {
        return new SeadSiteCreator(bugsSite).create();
    }

    public static class UpdateHelper {

        private SiteFromCodeAllowDeletedSite importSiteHelper;
        private boolean allowSiteUpdates;
        private SiteRepository siteRepository;

        private SiteLocationHandler siteLocationHandler;
        private String errorMessage = null;
        private BugsSite bugsData;
        private SeadSite foundSeadSite;

        public UpdateHelper(
                BugsSite bugsData,
                SiteLocationHandler siteLocationHandler,
                SiteFromCodeAllowDeletedSite importSiteHelper,
                boolean allowSiteUpdates,
                SiteRepository siteRepository) {
            this.bugsData = bugsData;
            this.siteLocationHandler = siteLocationHandler;
            this.importSiteHelper = importSiteHelper;
            this.allowSiteUpdates = allowSiteUpdates;
            this.siteRepository = siteRepository;
        }

        public boolean handle(){
            foundSeadSite = getFromTraceOrByNameAndLocation();
            if(foundSeadSite == null) {
                return false;
            } else if(!foundSeadSite.isErrorFree()){
                return true;
            } else {
                SiteUpdater updater = new SiteUpdater(bugsData, foundSeadSite);
                if(updater.needUpdates()) {
                    if (allowSiteUpdates) {
                        updater.doUpdates();
                    } else {
                        errorMessage = "Bugs data is updated but updates are disallowed.";
                    }
                }
            }
            return errorMessage == null;
        }

        private SeadSite getFromTraceOrByNameAndLocation(){
            SeadSite seadSiteFromTrace = importSiteHelper.getSeadSiteFromBugsCode(bugsData.getCode());
            if(seadSiteFromTrace != null){
                return seadSiteFromTrace;
            }
            List<SeadSite> possibleSites;
            if(!siteLocationHandler.anyCreatedLocations()){
                possibleSites = siteRepository.findByNameAndLocations(bugsData.getName(), siteLocationHandler.getLocations());
            } else {
                possibleSites = siteRepository.findAllByName(bugsData.getName());
            }
            if(possibleSites.size() == 1){
                errorMessage = "Site name exists for non-imported site";
            } else if(possibleSites.size() > 1){
                errorMessage = "More than one site found by name and location";
            }
            return null;
        }

        public String getErrorMessage(){
            return errorMessage;
        }

        public SeadSite getFoundSeadSite(){
            return foundSeadSite;
        }
    }
}
