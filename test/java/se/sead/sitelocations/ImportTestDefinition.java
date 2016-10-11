package se.sead.sitelocations;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.model.TestLocation;
import se.sead.model.TestSeadSite;
import se.sead.model.TestSiteLocation;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.repositories.SiteRepository;

import java.util.*;

public class ImportTestDefinition {

    private Map<String, SeadSite> sites;
    private Map<String, Location> locations;
    private LocationType administrativeLocationType;
    private LocationType countryType;

    ImportTestDefinition(SiteRepository siteRepository, LocationRepository locationRepository, LocationTypeRepository locationTypeRepository){
        setupSites(siteRepository);
        setupLocations(locationRepository);
        administrativeLocationType = locationTypeRepository.getAdministrativeRegionType();
        countryType = locationTypeRepository.getCountryType();
    }

    private void setupSites(SiteRepository siteRepository){
        sites = new HashMap<>(15);
        sites.put("all locations exists", siteRepository.findOne(1));
        sites.put("new region", siteRepository.findOne(2));
        sites.put("change country", siteRepository.findOne(3));
        sites.put("change region", siteRepository.findOne(4));
        sites.put("No locations stored", siteRepository.findOne(5));
        sites.put("Change country and region", siteRepository.findOne(6));
        sites.put("Localized region", siteRepository.findOne(7));
        sites.put("Region with same name different type", siteRepository.findOne(8));
        sites.put("Sead changed after bugs import", siteRepository.findOne(9));
        sites.put("Sead locations changed after bugs import", siteRepository.findOne(10));
        sites.put("Country does not exist", siteRepository.findOne(11));
        sites.put("Region does not exist", siteRepository.findOne(12));
        sites.put("Empty country", siteRepository.findOne(13));
        sites.put("Empty region", siteRepository.findOne(14));
        sites.put("New site", createNewSite());
    }

    private void setupLocations(LocationRepository repository){
        locations = new HashMap<>();
        locations.put("Country", repository.findOne(1));
        locations.put("Region", repository.findOne(3));
        locations.put("Other country", repository.findOne(2));
        locations.put("Other region", repository.findOne(4));
        locations.put("Moen", repository.findOne(6));
        locations.put("Skane", repository.findOne(7));
        locations.put("Region type", repository.findOne(5));
    }

    private SeadSite createNewSite() {
        return TestSeadSite.create(null, "New site", null, null, null, null, null);
    }

    public List<SiteLocation> getExpectedData(boolean canCreateCountry){
        List<SiteLocation> expectedData = new ArrayList<>();

        expectedData.addAll(getAllLocationsExistsLocations());
        expectedData.addAll(getNewRegionLocations());
        expectedData.addAll(getChangeCountryLocations());
        expectedData.addAll(getChangeRegionLocations());
        expectedData.addAll(getNoLocationsStoredLocations());
        expectedData.addAll(getChangeCountryAndRegionLocations());
        expectedData.addAll(getLocalizedRegionLocations());
        expectedData.addAll(getRegionWithSameNameDifferentTypeLocations());
        expectedData.addAll(getSiteChangedAfterImportLocations());
        expectedData.addAll(getSiteLocationsChangedAfterImportLocations());
        expectedData.addAll(getCountryDoesNotExistLocations(canCreateCountry));
        expectedData.addAll(getRegionDoesNotExistLocations());
        expectedData.addAll(getEmptyRegionLocations());
        expectedData.addAll(getNewSiteLocations());

        return expectedData;
    }

    private List<? extends SiteLocation> getAllLocationsExistsLocations() {
        SeadSite site = sites.get("all locations exists");
        return Arrays.asList(
                TestSiteLocation.create(1, locations.get("Country"), site),
                TestSiteLocation.create(2, locations.get("Region"), site)
        );
    }

    private List<? extends SiteLocation> getNewRegionLocations() {
        SeadSite site = sites.get("new region");
        return Arrays.asList(
                TestSiteLocation.create(3, locations.get("Country"), site),
                TestSiteLocation.create(null, locations.get("Region"), site)
        );
    }

    private List<? extends SiteLocation> getChangeCountryLocations() {
        SeadSite site = sites.get("change country");
        return Arrays.asList(
                TestSiteLocation.create(null, locations.get("Other country"), site),
                TestSiteLocation.create(5, locations.get("Region"), site)
        );
    }

    private List<? extends SiteLocation> getChangeRegionLocations() {
        SeadSite site = sites.get("change region");
        return Arrays.asList(
                TestSiteLocation.create(6, locations.get("Country"), site),
                TestSiteLocation.create(null, locations.get("Other region"), site)
        );
    }

    private List<? extends SiteLocation> getNoLocationsStoredLocations() {
        SeadSite site = sites.get("No locations stored");
        return Arrays.asList(
            TestSiteLocation.create(null, locations.get("Country"), site),
                TestSiteLocation.create(null, locations.get("Region"), site)
        );
    }

    private List<? extends SiteLocation> getChangeCountryAndRegionLocations() {
        SeadSite site = sites.get("Change country and region");
        return Arrays.asList(
            TestSiteLocation.create(null, locations.get("Other country"), site),
                TestSiteLocation.create(null, locations.get("Other region"), site)
        );
    }

    private List<? extends SiteLocation> getLocalizedRegionLocations() {
        SeadSite site = sites.get("Localized region");
        return Arrays.asList(
                TestSiteLocation.create(11, locations.get("Skane"), site),
                TestSiteLocation.create(null, locations.get("Moen"), site)
        );
    }

    private List<? extends SiteLocation> getRegionWithSameNameDifferentTypeLocations() {
        SeadSite site = sites.get("Region with same name different type");
        return Arrays.asList(
                TestSiteLocation.create(12, locations.get("Country"), site),
                TestSiteLocation.create(null, locations.get("Region type"), site)
        );
    }

    private List<? extends SiteLocation> getSiteChangedAfterImportLocations() {
        SeadSite site = sites.get("Sead changed after bugs import");
        return Arrays.asList(
                TestSiteLocation.create(14, locations.get("Country"), site),
                    TestSiteLocation.create(15, locations.get("Region"), site)
        );
    }

    private List<? extends SiteLocation> getSiteLocationsChangedAfterImportLocations() {
        SeadSite site = sites.get("Sead locations changed after bugs import");
        return Arrays.asList(
                TestSiteLocation.create(16, locations.get("Country"), site),
                TestSiteLocation.create(17, locations.get("Region"), site)
        );
    }

    private List<? extends SiteLocation> getCountryDoesNotExistLocations(boolean canCreateCountry) {
        SeadSite site = sites.get("Country does not exist");
        if(canCreateCountry) {
            return Arrays.asList(
                    TestSiteLocation.create(null, TestLocation.create(null, "Nonexisting", countryType), site),
                    TestSiteLocation.create(null, locations.get("Region"), site)
            );
        }
        return Collections.EMPTY_LIST;
    }

    private List<? extends SiteLocation> getRegionDoesNotExistLocations() {
        SeadSite site = sites.get("Region does not exist");
        return Arrays.asList(
                TestSiteLocation.create(null, locations.get("Country"), site),
                TestSiteLocation.create(null, TestLocation.create(null, "Nonexisting", administrativeLocationType), site)
        );
    }

    private List<? extends SiteLocation> getEmptyRegionLocations(){
        return Arrays.asList(
            TestSiteLocation.create(18, locations.get("Country"), sites.get("Empty region"))
        );
    }

    private List<? extends SiteLocation> getNewSiteLocations() {
        SeadSite site = sites.get("New site");
        return Arrays.asList(
                TestSiteLocation.create(null, locations.get("Country"), site),
                TestSiteLocation.create(null, locations.get("Region"), site)
        );
    }

}
