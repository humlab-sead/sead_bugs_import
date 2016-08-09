package se.sead.site;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@TestPropertySource(properties = { "allow.site.updates=false" })
public class SiteConverterUpdatesNotAllowedTest extends SiteConverterTest{

    protected String bugsDescription = "This description has changed";

    @Test
    public void updateDataForSiteWithSameNameAndSameLocation(){
        SeadSite storedSite = createAndStoreExpected();
        SeadSite convertedSite = convertedBugsSite();
        assertEquals("Bugs data is updated but updates are disallowed.", convertedSite.getErrors().get(0));
        assertEquals(storedSite, convertedSite);
    }

    protected SeadSite createAndStoreExpected(){
        Location country = createAndSaveLocation("Country", typeRepository.getCountryType());
        Location region = createAndSaveLocation("Region", typeRepository.getAdministrativeRegionType());
        SeadSite storedSite = createPlaceholderSite();
        return setLocations(siteRepository.saveOrUpdate(storedSite), country, region);
    }

    protected SeadSite convertedBugsSite(){
        BugsSite bugsSite = createBugsSite("Region", "Country");
        bugsSite.setInterp(bugsDescription);

        SeadSite seadSite = tableConverter.convertForDataRow(bugsSite);
        return seadSite;
    }
}
