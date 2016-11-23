package se.sead.rdbcodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = RDBCodesTypeTranslationTest.Config.class)
@Transactional
public class RDBCodesTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCategory(){
        createAndSaveTypeTranslator("Category", "A", "AB");
        BugsRDBCodes source = new BugsRDBCodes();
        source.setCategory("A");
        translationService.translateValues(source);
        assertEquals("AB", source.getCategory());
    }

    private void createAndSaveTypeTranslator(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TRDBCodes", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeRDBDefinition(){
        createAndSaveTypeTranslator("RDBDefinition", "A", "AB");
        BugsRDBCodes source = new BugsRDBCodes();
        source.setRdbDefinition("A");
        translationService.translateValues(source);
        assertEquals("AB", source.getRdbDefinition());
    }

    @Test
    public void changeRDBSystemCOde(){
        createAndSaveTypeTranslator("RDBSystemCode", "0", "1");
        BugsRDBCodes source = new BugsRDBCodes();
        source.setRdbSystemCode(0);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(1), source.getRdbSystemCode());
    }
}
