package se.sead.datasetcontacts.updater;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.datasetcontacts.updater.SiteContactReader;
import se.sead.testutils.DefaultConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SiteContactReaderTest {

    @TestConfiguration
    static class Config extends DefaultConfig {
        Config(){
            super("datasetcontacts/updater", "datasetcontactssite.mdb", "");
        }

        @Override
        protected String getDataFile() {
            return "";
        }
    }

    @Autowired
    private SiteContactReader provider;

    @Test
    public void noContacts(){
        SiteContactReader.SiteContactStringData siteContacts = getSiteContactStringData("SITE000004");
        assertNull(siteContacts.getIdentifiedBy());
        assertNull(siteContacts.getSpecimenRepository());
    }

    private SiteContactReader.SiteContactStringData getSiteContactStringData(String siteCode) {
        return provider.parse(siteCode);
    }

    @Test
    public void identifiedBy(){
        SiteContactReader.SiteContactStringData siteContacts = getSiteContactStringData("SITE000002");
        assertEquals("Archer", siteContacts.getIdentifiedBy());
        assertNull(siteContacts.getSpecimenRepository());
    }

    @Test
    public void specimenRepository(){
        SiteContactReader.SiteContactStringData siteContacts = getSiteContactStringData("SITE000003");
        assertEquals("University of Ottowa", siteContacts.getSpecimenRepository());
        assertNull(siteContacts.getIdentifiedBy());
    }

    @Test
    public void identifiedByAndspecimenRepository(){
        SiteContactReader.SiteContactStringData siteContacts = getSiteContactStringData("SITE000001");
        assertEquals("University of Ottowa", siteContacts.getSpecimenRepository());
        assertEquals("Archer", siteContacts.getIdentifiedBy());
    }

    @Test
    public void identifiedByMultiple(){
        SiteContactReader.SiteContactStringData siteContacts = getSiteContactStringData("SITE000005");
        assertEquals("Archer, Bookland", siteContacts.getIdentifiedBy());
        assertNull(siteContacts.getSpecimenRepository());
    }
}
