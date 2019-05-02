package se.sead.sitelocations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiteLocationTypeTranslationTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class SiteLocationTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeSiteCode() {
        createAndSaveTypeTranslation("SiteCODE", "Wrong", "Correct");
        BugsSiteLocation source = new BugsSiteLocation();
        source.setSiteCode("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSiteCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TSite", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeCountry(){
        createAndSaveTypeTranslation("Country", "Wrong", "Correct");
        BugsSiteLocation source = new BugsSiteLocation();
        source.setCountry("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountry());
    }

    @Test
    public void changeRegion(){
        createAndSaveTypeTranslation("Region", "Wrong", "Correct");
        BugsSiteLocation source = new BugsSiteLocation();
        source.setRegion("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRegion());
    }
}
