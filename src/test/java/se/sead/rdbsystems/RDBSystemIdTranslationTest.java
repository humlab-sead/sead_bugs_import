package se.sead.rdbsystems;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = RDBSystemIdTranslationTest.Config.class)
@Transactional
public class RDBSystemIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final Integer ID = 1;

    @Test
    public void changeSystem(){
        createAndSaveIdTranslation("RDBSystem", "Correct");
        BugsRDBSystem source = create();
        source.setRdbSystem("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRdbSystem());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TRDBSystems", String.valueOf(ID), column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private BugsRDBSystem create(){
        BugsRDBSystem source = new BugsRDBSystem();
        source.setRdbSystemCode(ID);
        return source;
    }

    @Test
    public void changeVersion(){
        createAndSaveIdTranslation("RDBVersion", "2");
        BugsRDBSystem source = create();
        source.setRdbVersion("1");
        translationService.translateValues(source);
        assertEquals("2", source.getRdbVersion());
    }

    @Test
    public void changeSystemDate(){
        createAndSaveIdTranslation("RDBSystemDate", "1900");
        BugsRDBSystem source = create();
        source.setRdbSystemDate(2000);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(1900), source.getRdbSystemDate());
    }

    @Test
    public void changeFirstPublished(){
        createAndSaveIdTranslation("RDBFirstPublished", "1900");
        BugsRDBSystem source = create();
        source.setRdbFirstPublished((short)2000);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("1900"), source.getRdbFirstPublished());
    }

    @Test
    public void changeRef(){
        createAndSaveIdTranslation("Ref", "Correct");
        BugsRDBSystem source = create();
        source.setRef("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRef());
    }

    @Test
    public void changeCountryCode(){
        createAndSaveIdTranslation("CountryCode", "Correct");
        BugsRDBSystem source = create();
        source.setCountryCode("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountryCode());
    }

}
