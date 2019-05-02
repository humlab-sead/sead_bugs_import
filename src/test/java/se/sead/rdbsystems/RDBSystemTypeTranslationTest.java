package se.sead.rdbsystems;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = RDBSystemTypeTranslationTest.Config.class)
@Transactional
public class RDBSystemTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeRDBSystem(){
        createAndSaveTypeTranslation("RDBSystem", "AB", "A");
        BugsRDBSystem source = new BugsRDBSystem();
        source.setRdbSystem("AB");
        translationService.translateValues(source);
        assertEquals("A", source.getRdbSystem());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TRDBSystems", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeVersion(){
        createAndSaveTypeTranslation("RDBVersion", "AB", "A");
        BugsRDBSystem source = new BugsRDBSystem();
        source.setRdbVersion("AB");
        translationService.translateValues(source);
        assertEquals("A", source.getRdbVersion());
    }

    @Test
    public void changePublicationYear(){
        createAndSaveTypeTranslation("RDBSystemDate", "20160101", "20161111");
        BugsRDBSystem source = new BugsRDBSystem();
        source.setRdbSystemDate(20160101);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(20161111), source.getRdbSystemDate());
    }

    @Test
    public void changeFirstPublished(){
        createAndSaveTypeTranslation("RDBFirstPublished", "2016", "2015");
        BugsRDBSystem source = new BugsRDBSystem();
        source.setRdbFirstPublished((short)2016);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("2015"), source.getRdbFirstPublished());
    }

    @Test
    public void changeRef(){
        createAndSaveTypeTranslation("Ref", "ABC", "AB");
        BugsRDBSystem source = new BugsRDBSystem();
        source.setRef("ABC");
        translationService.translateValues(source);
        assertEquals("AB", source.getRef());
    }

    @Test
    public void changeCountryCode(){
        createAndSaveTypeTranslation("CountryCode", "ABC", "AB");
        BugsRDBSystem source = new BugsRDBSystem();
        source.setCountryCode("ABC");
        translationService.translateValues(source);
        assertEquals("AB", source.getCountryCode());
    }
}
