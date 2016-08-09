package se.sead.site;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import se.sead.bugsimport.site.seadmodel.SeadSite;

import static org.junit.Assert.assertEquals;

@TestPropertySource(properties = { "allow.site.updates=true" })
public class SiteConverterUpdatesAllowed extends SiteConverterUpdatesNotAllowedTest {

    @Test
    public void updateDataForSiteWithSameNameAndSameLocation(){
        SeadSite storedSite = createAndStoreExpected();
        SeadSite convertedSite = convertedBugsSite();
        assertEquals(bugsDescription, convertedSite.getDescription());
    }

}
