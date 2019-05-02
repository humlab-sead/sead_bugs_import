package se.sead.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SampleTypeTranslationTest.Config.class)
@Transactional
public class SampleTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeSiteCode(){
        createAndSaveTypeTranslation("SiteCODE", "Wrong", "Correct");
        BugsSample source = new BugsSample();
        source.setSiteCode("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSiteCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TSample", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeX(){
        createAndSaveTypeTranslation("X", "0", "1");
        BugsSample source = new BugsSample();
        source.setX("0");
        translationService.translateValues(source);
        assertEquals("1", source.getX());
    }

    @Test
    public void changeY(){
        createAndSaveTypeTranslation("Y", "0", "1");
        BugsSample source = new BugsSample();
        source.setY("0");
        translationService.translateValues(source);
        assertEquals("1", source.getY());
    }

    @Test
    public void changeZorDepthTop(){
        createAndSaveTypeTranslation("ZorDepthTop", "0", "1");
        BugsSample source = new BugsSample();
        source.setZOrDepthTop(0d);
        translationService.translateValues(source);
        assertEquals(new Double(1d), source.getZOrDepthTop());
    }

    @Test
    public void changeZorDepthBottom(){
        createAndSaveTypeTranslation("ZorDepthBot", "0", "1");
        BugsSample source = new BugsSample();
        source.setZOrDepthBot(0d);
        translationService.translateValues(source);
        assertEquals(new Double(1d), source.getZOrDepthBot());
    }

    @Test
    public void changeRefNrContext(){
        createAndSaveTypeTranslation("RefNrContext", "Wrong", "Correct");
        BugsSample source = new BugsSample();
        source.setRefNrContext("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRefNrContext());
    }

    @Test
    public void changeCountsheetCode(){
        createAndSaveTypeTranslation("CountsheetCODE", "Wrong", "Correct");
        BugsSample source = new BugsSample();
        source.setCountSheetCode("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountSheetCode());

    }
}
