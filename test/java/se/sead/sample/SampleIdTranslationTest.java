package se.sead.sample;

import org.junit.Before;
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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SampleIdTranslationTest.Config.class)
@Transactional
public class SampleIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "SAMPLE_ID";
    private BugsSample source;

    @Before
    public void setup(){
        source = new BugsSample();
        source.setSampleCode(ID);
    }

    @Test
    public void changeSiteCode(){
        createAndSaveIdTranslation("SiteCODE", "Correct");
        source.setSiteCode("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSiteCode());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TSample", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeX(){
        createAndSaveIdTranslation("X", "1");
        source.setX("Wrong");
        translationService.translateValues(source);
        assertEquals("1", source.getX());
    }

    @Test
    public void changeY(){
        createAndSaveIdTranslation("Y", "1");
        source.setY("Wrong");
        translationService.translateValues(source);
        assertEquals("1", source.getY());
    }

    @Test
    public void changeZOrDepthTop(){
        createAndSaveIdTranslation("ZorDepthTop", "1");
        source.setZOrDepthTop(0d);
        translationService.translateValues(source);
        assertEquals(new Double(1d), source.getZOrDepthTop());
    }

    @Test
    public void changeZOrdDepthBot(){
        createAndSaveIdTranslation("ZorDepthBot", "1");
        source.setZOrDepthBot(0d);
        translationService.translateValues(source);
        assertEquals(new Double(1d), source.getZOrDepthBot());
    }

    @Test
    public void changeRefNrContext(){
        createAndSaveIdTranslation("RefNrContext", "Correct");
        source.setRefNrContext("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRefNrContext());
    }

    @Test
    public void changeCountsheetCode(){
        createAndSaveIdTranslation("CountsheetCODE", "Correct");
        source.setCountSheetCode("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountSheetCode());
    }
}
