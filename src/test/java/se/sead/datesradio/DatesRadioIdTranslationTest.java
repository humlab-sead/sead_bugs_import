package se.sead.datesradio;

import org.assertj.core.internal.Dates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatesRadioIdTranslationTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class DatesRadioIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String DEFAULT_ID = "DATE00001";

    @Test
    public void changeSampleCODE(){
        createAndSaveIdBasedTranslation("SampleCODE", "SAMP0001");
        DatesRadio source = create();
        source.setSampleCode("SAMP");
        translationService.translateValues(source);
        assertEquals("SAMP0001", source.getSampleCode());
    }

    private void createAndSaveIdBasedTranslation(
            String targetColumn,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TDatesRadio", DEFAULT_ID, targetColumn, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private DatesRadio create(){
        DatesRadio source = new DatesRadio();
        source.setDateCode(DEFAULT_ID);
        return source;
    }

    @Test
    public void changeLabNr(){
        createAndSaveIdBasedTranslation("LabNr", "A-120");
        DatesRadio source = create();
        source.setLabNr("AA-120");
        translationService.translateValues(source);
        assertEquals("A-120", source.getLabNr());
    }

    @Test
    public void changeUncertainty(){
        createAndSaveIdBasedTranslation("Uncertainty", "?");
        DatesRadio source = create();
        source.setUncertainty("Unknown");
        translationService.translateValues(source);
        assertEquals("?", source.getUncertainty());
    }

    @Test
    public void changeDate(){
        createAndSaveIdBasedTranslation("Date", "1000");
        DatesRadio source = create();
        source.setDate(458);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(1000), source.getDate());
    }

    @Test
    public void changeAgeErrorOrPlusError(){
        createAndSaveIdBasedTranslation("AgeErrorOrPlusError", "1000");
        DatesRadio source = create();
        source.setAgeErrorOrPlusError((short)0);
        translationService.translateValues(source);
        assertEquals((short)1000, (short) source.getAgeErrorOrPlusError());
    }

    @Test
    public void changeAgeErrorMinus(){
        createAndSaveIdBasedTranslation("AgeErrorMinus", "1000");
        DatesRadio source = create();
        source.setAgeErrorMinus(0);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(1000), source.getAgeErrorMinus());
    }

    @Test
    public void changeDatingMethod(){
        createAndSaveIdBasedTranslation("DatingMethod", "Correct method");
        DatesRadio source = create();
        source.setDatingMethod("Wrong method");
        translationService.translateValues(source);
        assertEquals("Correct method", source.getDatingMethod());
    }

    @Test
    public void changeLabID(){
        createAndSaveIdBasedTranslation("LabID", "Q");
        DatesRadio source = create();
        source.setLabId("Q*+");
        translationService.translateValues(source);
        assertEquals("Q", source.getLabId());
    }

    @Test
    public void changeNotes(){
        createAndSaveIdBasedTranslation("Notes", "Correct Note");
        DatesRadio source = create();
        source.setDatingMethod("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct Note", source.getNotes());
    }
}
