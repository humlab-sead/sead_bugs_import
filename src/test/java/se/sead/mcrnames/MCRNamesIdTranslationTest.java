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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = MCRNamesIdTranslationTest.Config.class)
@Transactional
public class MCRNamesIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final Double ID = 1d;

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation("CODE", "10");
        BugsMCRNames source = create();
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TMCRNames", ID.toString(), column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private BugsMCRNames create(){
        BugsMCRNames source = new BugsMCRNames();
        source.setCode(ID);
        return source;
    }

    @Test
    public void changeMCRNumber(){
        createAndSaveIdTranslation("MCRNumber", "100");
        BugsMCRNames source = create();
        source.setMcrNumber((short)10);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getMcrNumber());
    }

    @Test
    public void changeMCRName(){
        createAndSaveIdTranslation("MCRName", "Correct name");
        BugsMCRNames source = create();
        source.setMcrName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getMcrName());
    }

    @Test
    public void changeCompareStatus(){
        createAndSaveIdTranslation("CompareStatus", "Correct name");
        BugsMCRNames source = create();
        source.setCompareStatus("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getCompareStatus());
    }

    @Test
    public void changetempCODE(){
        createAndSaveIdTranslation("tempCODE", "10");
        BugsMCRNames source = create();
        source.setTempCode(1d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getTempCode());
    }

    @Test
    public void changeMCRNameTrim(){
        createAndSaveIdTranslation("MCRNameTrim", "Correct name");
        BugsMCRNames source = create();
        source.setMcrNameTrim("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getMcrNameTrim());
    }
}
