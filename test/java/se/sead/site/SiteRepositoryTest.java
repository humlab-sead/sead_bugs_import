package se.sead.site;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.country.seadmodel.LocationType;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.site.seadmodel.SiteLocation;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.repositories.SiteLocationRepository;
import se.sead.repositories.SiteRepository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, SiteRepositoryTest.Config.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SiteRepositoryTest {

    @Configuration
    public static class Config  {

        @Bean
        public DataSource createDataSource() {
            return DataSourceFactory.createDefault("country/countries.sql");
        }

    }

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteLocationRepository siteLocationRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;

    private LocationHandler locationHandler;

    @Before
    public void setup(){
        locationHandler = new LocationHandler(locationTypeRepository, locationRepository);
    }

    @Test
    public void findByNameAndLocationsNoLocations(){
        SeadSite testSite = createSite("site", (String)null, null);
        List<SeadSite> foundSite = siteRepository.findByNameAndLocations("site", Collections.EMPTY_LIST);
        assertEquals(0, foundSite.size());
    }

    private SeadSite createSite(String siteName, String countryName, String regionName) {
        List<Location> locations = new ArrayList<>();
        if(countryName != null){
            locations.add(locationHandler.insertCountry(countryName));
        }
        if(regionName != null){
            locations.add(locationHandler.insertRegion(regionName));
        }
        SeadSite site = createSimpleSite(siteName);
        return saveSiteLocations(siteRepository.saveOrUpdate(site), locations.toArray(new Location[locations.size()]));
    }

    private SeadSite createSimpleSite(String siteName) {
        SeadSite site = new SeadSite();
        site.setName(siteName);
        return site;
    }

    private SeadSite saveSiteLocations(SeadSite site, Location... locations){
        if(locations != null) {
            List<SiteLocation> savedLocations = new ArrayList<>(locations.length);
            for (Location location :
                    locations) {
                savedLocations.add(store(site, location));
            }
            site.setSiteLocations(savedLocations);
        }
        return site;
    }

    private SiteLocation store(SeadSite site, Location location){
        SiteLocation siteLocation = new SiteLocation();
        siteLocation.setLocation(location);
        siteLocation.setSite(site);
        return siteLocationRepository.saveOrUpdate(siteLocation);
    }

    @Test
    public void oneLocation() {
        SeadSite testSite = createSite("site1", "country", null);
        Location country = locationRepository.findCountryByName("country");
        List<SeadSite> foundSites = siteRepository.findByNameAndLocations("site1", Arrays.asList(country));
        assertEquals(1, foundSites.size());
        assertEquals(testSite, foundSites.get(0));
    }

    @Test
    public void twoLocations(){
        SeadSite testSite = createSite("site1", "country", "region");
        Location country = locationRepository.findCountryByName("country");
        Location region = locationHandler.findFirstRegionByName("region");
        List<SeadSite> foundSites = siteRepository.findByNameAndLocations("site1", Arrays.asList(country, region));
        assertEquals(1, foundSites.size());
        assertEquals(testSite, foundSites.get(0));
    }

    @Test
    public void severalSitesDifferentLocations(){
        SeadSite site1 = createSite("site1", "country", "region");
        SeadSite site2 = createSite("site2", "country2", "region2");
        Location country2 = locationRepository.findCountryByName("country2");
        Location region2 = locationHandler.findFirstRegionByName("region2");
        List<SeadSite> foundSites = siteRepository.findByNameAndLocations("site2", Arrays.asList(country2, region2));
        assertEquals(1, foundSites.size());
        assertEquals(site2, foundSites.get(0));
    }

    @Test
    public void severalSitesSameLocations(){
        SeadSite site1 = createSite("site1", "country", "region");
        Location country = locationRepository.findCountryByName("country");
        Location region = locationHandler.findFirstRegionByName("region");
        SeadSite site2 = createSite("site2", country, region);
        List<SeadSite> foundSites = siteRepository.findByNameAndLocations("site1", Arrays.asList(country, region));
        assertEquals(1, foundSites.size());
        assertEquals(site1, foundSites.get(0));
    }

    private SeadSite createSite(String siteName, Location... locations){
        SeadSite site = createSimpleSite(siteName);
        return saveSiteLocations(siteRepository.saveOrUpdate(site), locations);
    }

    @Test
    public void severalSitesWrongLocations(){
        SeadSite site1 = createSite("site1", "country", "region");
        SeadSite site2 = createSite("site2", "country2", "region2");
        Location country2 = locationRepository.findCountryByName("country2");
        Location region2 = locationHandler.findFirstRegionByName("region2");
        List<SeadSite> foundSites = siteRepository.findByNameAndLocations("site1", Arrays.asList(country2, region2));
        assertEquals(0, foundSites.size());
    }

    @Test
    public void severalSitesSameNameSameLocations(){
        Location country = locationHandler.insertCountry("country");
        Location region = locationHandler.insertRegion("region");
        SeadSite site1 = createSite("site1", new BigDecimal("1"), country, region);
        SeadSite site2 = createSite("site1", new BigDecimal("2"), country, region);
        List<SeadSite> foundSites = siteRepository.findByNameAndLocations("site1", Arrays.asList(country, region));
        assertEquals(2, foundSites.size());
        Collections.sort(foundSites, (o1, o2) -> o1.getAltitude().compareTo(o2.getAltitude()));
        assertEquals(site1, foundSites.get(0));
        assertEquals(site2, foundSites.get(1));
    }

    public SeadSite createSite(String siteName, BigDecimal altitude, Location... locations){
        SeadSite site = createSimpleSite(siteName);
        site.setAltitude(altitude);
        return saveSiteLocations(siteRepository.saveOrUpdate(site), locations);
    }

    private static class LocationHandler {
        private LocationType countryType;
        private LocationType regionType;

        private LocationRepository locationRepository;

        LocationHandler(LocationTypeRepository locationTypeRepository, LocationRepository locationRepository){
            countryType = locationTypeRepository.getCountryType();
            regionType = locationTypeRepository.getAdministrativeRegionType();
            this.locationRepository = locationRepository;
        }

        Location insertCountry(String name){
            return insertLocation(name, countryType);
        }

        private Location insertLocation(String locationName, LocationType locationType) {
            Location location = new Location();
            location.setName(locationName);
            location.setType(locationType);
            return locationRepository.saveOrUpdate(location);
        }

        Location insertRegion(String name){
            return insertLocation(name, regionType);
        }

        Location findFirstRegionByName(String name){
            List<Location> allByName = locationRepository.findAllByName(name);
            Location region = null;
            for (Location potentialLocation :
                    allByName) {
                if (potentialLocation.getType().equals(regionType)){
                    if(region == null) {
                        region = potentialLocation;
                    } else {
                        throw new IllegalStateException("Too many regions with same name");
                    }
                }
            }
            if(region != null){
                return region;
            }
            throw new IllegalArgumentException("No region found named: " + name);
        }
    }
}
