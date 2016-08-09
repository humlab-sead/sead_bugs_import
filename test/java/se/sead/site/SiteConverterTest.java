package se.sead.site;

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
import se.sead.bugsimport.site.conversion.BugsSiteTableConverter;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.site.seadmodel.SiteLocation;
import se.sead.repositories.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static se.sead.BigDecimalDefinition.SEAD_MATH_CONTEXT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, SiteConverterTest.Config.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SiteConverterTest {

    @Configuration
    public static class Config {

        @Bean
        public DataSource createDataSource(){
            return DataSourceFactory.createDefault("site/locationtypes.sql");
        }

    }

    private Float placeholderLatitude = 59.6700f;
    private Float placeholderLongitude = 15.00765f;
    private Float placeholderAltitude = 59f;
    private String placeholderLatitudeString = "59.6700";
    private String placeholderLongitudeString = "15.00765";
    private String placeholderAltitudeString = "59";

    private String placeholderEngDescription = "A description of a site without strange characters.";

    @Autowired
    protected SiteRepository siteRepository;
    @Autowired
    protected LocationTypeRepository typeRepository;
    @Autowired
    protected LocationRepository locationRepository;
    @Autowired
    protected SiteLocationRepository siteLocationRepository;
    @Autowired
    protected TypeTranslationRepository typeTranslationRepository;

    @Autowired
    protected BugsSiteTableConverter tableConverter;

    @Test
    public void importExistingByNameSiteDisregardLocations(){
        SeadSite existingSite = createPlaceholderSite();
        siteRepository.saveOrUpdate(existingSite);

        BugsSite bugsVersion = createBugsSite(null, null);
        SeadSite importedSite = tableConverter.convertForDataRow(bugsVersion);
        assertEquals(existingSite, importedSite);
    }

    protected SeadSite createPlaceholderSite() {
        SeadSite existingSite = new SeadSite();
        existingSite.setName("existing site");
        existingSite.setNationalSiteIdentifier("id");
        existingSite.setAltitude(convertToBigDecimal(placeholderAltitudeString));
        existingSite.setLatitude(convertToBigDecimal(placeholderLatitudeString));
        existingSite.setLongitude(convertToBigDecimal(placeholderLongitudeString));
        existingSite.setDescription(placeholderEngDescription);
        return existingSite;
    }

    protected static BigDecimal convertToBigDecimal(Float value){
        if(value != null){
            return new BigDecimal(value, SEAD_MATH_CONTEXT);
        } else {
            return null;
        }
    }

    protected static BigDecimal convertToBigDecimal(String value){
        if(value == null){
            return null;
        } else {
            return new BigDecimal(value, SEAD_MATH_CONTEXT);
        }
    }

    protected BugsSite createBugsSite(String region, String country) {
        return SiteTestDefinition.create(
                "SITE00001",
                "existing site",
                region,
                country,
                "id",
                placeholderLatitude,
                placeholderLongitude,
                placeholderAltitude,
                "Some person",
                placeholderEngDescription,
                "Some university");
    }

    @Test
    public void importExistingByNameAndLocation(){
        Location countryLocation = createAndSaveLocation("Country", typeRepository.getCountryType());
        Location regionLocation = createAndSaveLocation("Region", typeRepository.getAdministrativeRegionType());
        Location wrongCountryLocation = createAndSaveLocation("Country2", typeRepository.getCountryType());

        SeadSite namedTargetLocationsSite = createPlaceholderSite();
        namedTargetLocationsSite = setLocations(siteRepository.saveOrUpdate(namedTargetLocationsSite), countryLocation, regionLocation);

        SeadSite wrongSite = createPlaceholderSite();
        wrongSite.setDescription("This is not the site we are searching for");
        wrongSite = setLocations(siteRepository.saveOrUpdate(wrongSite), regionLocation, wrongCountryLocation);

        BugsSite importVersion = createBugsSite("Region", "Country");
        SeadSite convertedSite = tableConverter.convertForDataRow(importVersion);
        assertEquals(namedTargetLocationsSite, convertedSite);
        assertNotEquals(wrongSite, convertedSite);
    }

    protected Location createAndSaveLocation(String name, LocationType type) {
        Location targetCountry = new Location();
        targetCountry.setName(name);
        targetCountry.setType(type);
        return locationRepository.saveOrUpdate(targetCountry);
    }

    SeadSite setLocations(SeadSite site, Location... locations){
        List<SiteLocation> siteLocations = new ArrayList<>(locations.length);
        for (Location location :
                locations) {
            SiteLocation siteLocation = new SiteLocation();
            siteLocation.setLocation(location);
            siteLocation.setSite(site);
            siteLocations.add(siteLocationRepository.saveOrUpdate(siteLocation));
        }
        site.setSiteLocations(siteLocations);
        return site;
    }

    @Test
    public void noNameNotAllowed(){
        BugsSite importVersion = createBugsSite("Region", "Country");
        importVersion.setName("");
        SeadSite imported = tableConverter.convertForDataRow(importVersion);
        assertTrue(imported.getErrors().toString().contains("Site name cannot be empty"));
    }
}
