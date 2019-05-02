package se.sead.sitereferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugs.BugsColumn;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SiteRefIdTranslationTest.Config.class)
@Transactional
public class SiteRefIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "{SITE_ID,Reference}";
    private BugsSiteRef source;

    @Before
    public void setup(){
        source = new BugsSiteRef();
        source.setSiteCode("SITE_ID");
        source.setRef("Reference");
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
        IdBasedTranslation translation = IdTranslationBuilder.create("TSiteRef", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeRef(){
        createAndSaveIdTranslation("Ref", "Corrected");
        translationService.translateValues(source);
        assertEquals("Corrected", source.getRef());
    }
}
