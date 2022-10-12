package se.sead.rdb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = RDBIdTranslationTest.Config.class)
@Transactional
public class RDBIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation("1.0", "CODE", "10");
        BugsRDB source = create();
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private void createAndSaveIdTranslation(
            String identifier,
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TRDB", identifier, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private BugsRDB create(){
        BugsRDB source = new BugsRDB();
        source.setCode(1d);
        source.setRdbCode(1);
        source.setCountryCode("UK");
        return source;
    }

    @Test
    public void changeCountryCode(){
        createAndSaveIdTranslation("1.0", "CountryCode", "Swe");
        BugsRDB source = create();
        translationService.translateValues(source);
        assertEquals("Swe", source.getCountryCode());
    }

    @Test
    public void changeRDBCode(){
        createAndSaveIdTranslation("1.0", "RDBCode", "2");
        BugsRDB source = create();
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(2), source.getRdbCode());
    }
}
