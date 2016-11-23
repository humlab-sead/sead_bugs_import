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


    @Test
    public void  changeCategory(){
        createAndSaveIdTranslation("Category", "A");
        BugsRDBCodes source = create();
        source.setCategory("AB");
        translationService.translateValues(source);
        assertEquals("A", source.getCategory());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TRDBCodes", "1", column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private BugsRDBCodes create(){
        BugsRDBCodes source = new BugsRDBCodes();
        source.setRdbCode(1);
        return source;
    }

    @Test
    public void changeRDBDefinition(){
        createAndSaveIdTranslation("RDBDefinition", "Correct");
        BugsRDBCodes source = create();
        source.setRdbDefinition("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRdbDefinition());
    }

    @Test
    public void changeRDBSystemCode(){
        createAndSaveIdTranslation("RDBSystemCode", "1");
        BugsRDBCodes source = create();
        source.setRdbSystemCode(0);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(1), source.getRdbSystemCode());
    }
}
