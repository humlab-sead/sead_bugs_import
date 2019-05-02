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
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = RDBTypeTranslationTest.Config.class)
@Transactional
public class RDBTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1", "10");
        BugsRDB source = new BugsRDB();
        source.setCode(1d);
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TRDB", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeCountryCode(){
        createAndSaveTypeTranslation("CountryCode", "Wrong code", "Correct code");
        BugsRDB source = new BugsRDB();
        source.setCountryCode("Wrong code");
        translationService.translateValues(source);
        assertEquals("Correct code", source.getCountryCode());
    }

    @Test
    public void changeRDBCode(){
        createAndSaveTypeTranslation("RDBCode", "1", "10");
        BugsRDB source = new BugsRDB();
        source.setRdbCode(1);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(10), source.getRdbCode());
    }
}
