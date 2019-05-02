package se.sead.countsheets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CountsheetIdTranslationTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class CountsheetIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeName(){
        createAndSaveIdTranslation("COUN0001", "CountsheetName", "Correct name");
        Countsheet source = new Countsheet();
        source.setCode("COUN0001");
        source.setName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getName());
    }

    private void createAndSaveIdTranslation(
            String bugsDefinition,
            String targetColumn,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TCountsheet", bugsDefinition, targetColumn, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeSiteCode(){
        createAndSaveIdTranslation("COUN0001", "SiteCODE", "SITE0001");
        Countsheet source = new Countsheet();
        source.setCode("COUN0001");
        source.setSiteCode("SITE");
        translationService.translateValues(source);
        assertEquals("SITE0001", source.getSiteCode());
    }

    @Test
    public void changeSheetContext(){
        createAndSaveIdTranslation("COUN0001", "SheetContext", "context");
        Countsheet source = new Countsheet();
        source.setCode("COUN0001");
        source.setContext("Fail content");
        translationService.translateValues(source);
        assertEquals("context", source.getContext());
    }

    @Test
    public void changeSheetType(){
        createAndSaveIdTranslation("COUN0001", "SheetType", "type");
        Countsheet source = new Countsheet();
        source.setCode("COUN0001");
        source.setType("wrong type");
        translationService.translateValues(source);
        assertEquals("type", source.getType());
    }
}
