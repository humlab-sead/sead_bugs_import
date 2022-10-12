package se.sead.datescalendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes=DatesCalenderTypeTranslationTest.Config.class)
@Transactional
public class DatesCalenderTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {

    }

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeSampleCode(){
        createAndSaveTypeTranslation("SampleCODE", "SAMP0001", "SampleCODE", "SAMP");
        DatesCalendar source = new DatesCalendar();
        source.setSample("SAMP");
        translationService.translateValues(source);
        assertEquals("SAMP0001", source.getSample());
    }

    private void createAndSaveTypeTranslation(
            String sourceColumn,
            String replacementValue,
            String targetColumn,
            String triggerValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create(
                "TDatesCalendar",
                sourceColumn,
                replacementValue,
                targetColumn,
                triggerValue
        );
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeUncertainty(){
        createAndSaveTypeTranslation("Uncertainty", "From ca.", "Uncertainty", "From ca");
        DatesCalendar source = new DatesCalendar();
        source.setUncertainty("From ca");
        translationService.translateValues(source);
        assertEquals("From ca.", source.getUncertainty());
    }

    @Test
    public void changeCalendarCODE(){
        createAndSaveTypeTranslation("CalendarCODE", "CALE0001", "CalendarCODE", "CALE0000");
        DatesCalendar source = new DatesCalendar();
        source.setCalendarCODE("CALE0000");
        translationService.translateValues(source);
        assertEquals("CALE0001", source.getCalendarCODE());
    }

    @Test
    public void changeDate(){
        createAndSaveTypeTranslation("Date", "1000", "Date", "10");
        DatesCalendar source = new DatesCalendar();
        source.setDate(10);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(1000), source.getDate());
    }

    @Test
    public void changeBCADBP(){
        createAndSaveTypeTranslation("BCADBP", "AD", "BCADBP", "ADD");
        DatesCalendar source = new DatesCalendar();
        source.setBcadbp("ADD");
        translationService.translateValues(source);
        assertEquals("AD", source.getBcadbp());
    }

    @Test
    public void changeDatingMethod(){
        createAndSaveTypeTranslation("DatingMethod", "Correct method", "DatingMethod", "Wrong method");
        DatesCalendar source = new DatesCalendar();
        source.setDatingMethod("Wrong method");
        translationService.translateValues(source);
        assertEquals("Correct method", source.getDatingMethod());
    }

    @Test
    public void changeNotes(){
        createAndSaveTypeTranslation("Notes", "Note", "Notes", "nn");
        DatesCalendar source = new DatesCalendar();
        source.setNotes("nn");
        translationService.translateValues(source);
        assertEquals("Note", source.getNotes());
    }
}
