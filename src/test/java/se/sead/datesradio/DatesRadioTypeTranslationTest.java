package se.sead.datesradio;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = DatesRadioTypeTranslationTest.Config.class)
@Transactional
public class DatesRadioTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeDateCODE(){
        createAndSaveTypeTranslation("DateCODE", "DATE00001", "DATE");
        DatesRadio source = new DatesRadio();
        source.setDateCode("DATE");
        translationService.translateValues(source);
        assertEquals("DATE00001", source.getDateCode());
    }

    private void createAndSaveTypeTranslation(
        String column,
        String replacementValue,
        String triggerValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TDatesRadio", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeSampleCODE(){
        createAndSaveTypeTranslation("SampleCODE", "SAMP0001", "SAMP");
        DatesRadio source = new DatesRadio();
        source.setSampleCode("SAMP");
        translationService.translateValues(source);
        assertEquals("SAMP0001", source.getSampleCode());
    }

    @Test
    public void changeLabNr(){
        createAndSaveTypeTranslation("LabNr", "ABCD1", "q111");
        DatesRadio source = new DatesRadio();
        source.setLabNr("q111");
        translationService.translateValues(source);
        assertEquals("ABCD1", source.getLabNr());
    }

    @Test
    public void changeUncertainty(){
        createAndSaveTypeTranslation("Uncertainty", "?", "unknown");
        DatesRadio source = new DatesRadio();
        source.setUncertainty("unknown");
        translationService.translateValues(source);
        assertEquals("?", source.getUncertainty());
    }

    @Test
    public void changeDate(){
        createAndSaveTypeTranslation("Date", "1000", "0");
        DatesRadio source = new DatesRadio();
        source.setDate(0);
        translationService.translateValues(source);
        assertEquals(new Integer(1000), source.getDate());
    }

    @Test
    public void changeAgeErrorOrPlusError(){
        createAndSaveTypeTranslation("AgeErrorOrPlusError", "200", "2");
        DatesRadio source = new DatesRadio();
        source.setAgeErrorOrPlusError((short)2);
        translationService.translateValues(source);
        assertEquals(new Short((short)200), source.getAgeErrorOrPlusError());
    }

    @Test
    public void changeAgeErrorMinus(){
        createAndSaveTypeTranslation("AgeErrorMinus", "200", "2");
        DatesRadio source = new DatesRadio();
        source.setAgeErrorMinus(2);
        translationService.translateValues(source);
        assertEquals(new Integer(200), source.getAgeErrorMinus());
    }

    @Test
    public void changeDatingMethod(){
        createAndSaveTypeTranslation("DatingMethod", "Correct method", "Wrong method");
        DatesRadio source = new DatesRadio();
        source.setDatingMethod("Wrong method");
        translationService.translateValues(source);
        assertEquals("Correct method", source.getDatingMethod());
    }

    @Test
    public void changeMaterialType(){
        createAndSaveTypeTranslation("MaterialType", "Correct type", "Wrong type");
        DatesRadio source = new DatesRadio();
        source.setMaterialType("Wrong type");
        translationService.translateValues(source);
        assertEquals("Correct type", source.getMaterialType());
    }

    @Test
    public void changeLabID(){
        createAndSaveTypeTranslation("LabID", "Correct", "Wrong");
        DatesRadio source = new DatesRadio();
        source.setLabId("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getLabId());
    }

    @Test
    public void changeNotes(){
        createAndSaveTypeTranslation("Notes", "Correct", "Wrong");
        DatesRadio source = new DatesRadio();
        source.setNotes("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getNotes());
    }
}
