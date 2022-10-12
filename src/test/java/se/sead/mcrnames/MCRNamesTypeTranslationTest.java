package se.sead.mcrnames;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = MCRNamesTypeTranslationTest.Config.class)
@Transactional
public class MCRNamesTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1", "10");
        BugsMCRNames source = new BugsMCRNames();
        source.setCode(1d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TMCRNames", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeMCRNumber(){
        createAndSaveTypeTranslation("MCRNumber", "1", "10");
        BugsMCRNames source = new BugsMCRNames();
        source.setMcrNumber((short)1);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("10"), source.getMcrNumber());
    }

    @Test
    public void changeMCRName(){
        createAndSaveTypeTranslation("MCRName", "Wrong name", "Correct name");
        BugsMCRNames source = new BugsMCRNames();
        source.setMcrName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getMcrName());
    }

    @Test
    public void changeCompareStatus(){
        createAndSaveTypeTranslation("CompareStatus", "Wrong status", "Correct status");
        BugsMCRNames source = new BugsMCRNames();
        source.setCompareStatus("Wrong status");
        translationService.translateValues(source);
        assertEquals("Correct status", source.getCompareStatus());
    }

    @Test
    public void changeTempCODE(){
        createAndSaveTypeTranslation("tempCODE", "1", "10");
        BugsMCRNames source = new BugsMCRNames();
        source.setTempCode(1d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getTempCode());
    }

    @Test
    public void changeMCRNameTrim(){
        createAndSaveTypeTranslation("MCRNameTrim", "Wrong name", "Correct name");
        BugsMCRNames source = new BugsMCRNames();
        source.setMcrNameTrim("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getMcrNameTrim());
    }
}
