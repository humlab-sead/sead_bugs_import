package se.sead.sitelocations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SiteLocationIdTranslationTest.Config.class)
@Transactional
public class SiteLocationIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    private static final String ID = "SITE_ID";

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private BugsSiteLocation source;

    @Before
    public void setup(){
        source = new BugsSiteLocation();
        source.setSiteCode(ID);
        source.setCountry("Wrong");
        source.setRegion("Wrong");
    }

    @Test
    public void changeSiteCODE(){
        createAndSaveIdTranslation("SiteCODE", "SITE0001");
        translationService.translateValues(source);
        assertEquals("SITE0001", source.getSiteCode());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TSite", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeCountry(){
        createAndSaveIdTranslation("Country", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountry());
    }

    @Test
    public void changeRegion(){
        createAndSaveIdTranslation("Region", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRegion());
    }

    @Test
    public void changeRegionWhenActiveSiteChangesArePresent(){
        createAndSaveIdTranslation("SiteName", "Some value");
        createAndSaveIdTranslation("Country", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountry());
    }
}
