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
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CountsheetTypeTranslationTest.Config.class)
@ActiveProfiles("test")
public class CountsheetTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {
    }

    @Autowired
    private TypeTranslationRepository typeTranslationRepository;

    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    @Transactional
    public void typeTranslationSheetContext(){
        createAndSaveTypeTranslation("SheetContext", "Archaeological site", "SheetContext", "Archaeological context");
        Countsheet source = new Countsheet();
        source.setContext("Archaeological context");
        translationService.translateValues(source);
        assertEquals("Archaeological site", source.getContext());
    }

    protected void createAndSaveTypeTranslation(
            String sourceColumnName,
            String replacementValue,
            String targetColumn,
            String triggeringColumnValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TCountsheet", sourceColumnName, replacementValue, targetColumn, triggeringColumnValue);
        typeTranslationRepository.saveOrUpdate(translation);
    }

    @Test
    @Transactional
    public void typeTranslationSiteCode(){
        createAndSaveTypeTranslation("SiteCODE", "SITE0002", "SiteCODE", "SITE0001");
        Countsheet source = new Countsheet();
        source.setSiteCode("SITE0001");
        translationService.translateValues(source);
        assertEquals("SITE0002", source.getSiteCode());
    }

    @Test
    @Transactional
    public void typeTranslationSheetType(){
        createAndSaveTypeTranslation("SheetType", "Presence/Absence", "SheetType", "Presence");
        Countsheet source = new Countsheet();
        source.setType("Presence");
        translationService.translateValues(source);
        assertEquals("Presence/Absence", source.getType());
    }

    @Test
    @Transactional
    public void typeTranslationCountsheetName(){
        createAndSaveTypeTranslation("CountsheetName", "Correct name", "CountsheetName", "Wrong name");
        Countsheet source = new Countsheet();
        source.setName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getName());
    }
}
