package se.sead.datesperiod;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = DatesPeriodTypeTranslationTest.Config.class)
@Transactional
public class DatesPeriodTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changePeriodDateCode(){
        createAndSaveTypeTranslation("PeriodDateCODE", "PERI00001", "PERI");
        DatesPeriod source = new DatesPeriod();
        source.setPeriodDateCode("PERI");
        translationService.translateValues(source);
        assertEquals("PERI00001", source.getPeriodDateCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String replacementValue,
            String triggerValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create(
                "TDatesPeriod",
                column,
                replacementValue,
                column,
                triggerValue
        );
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeSampleCODE(){
        createAndSaveTypeTranslation("SampleCODE", "SAMP00001", "SAMP");
        DatesPeriod source = new DatesPeriod();
        source.setSampleCode("SAMP");
        translationService.translateValues(source);
        assertEquals("SAMP00001", source.getSampleCode());
    }

    @Test
    public void changeUncertainty(){
        createAndSaveTypeTranslation("Uncertainty", "From ca.", "From ca");
        DatesPeriod source = new DatesPeriod();
        source.setUncertainty("From ca");
        translationService.translateValues(source);
        assertEquals("From ca.", source.getUncertainty());
    }

    @Test
    public void changePeriodCODE(){
        createAndSaveTypeTranslation("PeriodCODE", "AA", "aa");
        DatesPeriod source = new DatesPeriod();
        source.setPeriodCode("aa");
        translationService.translateValues(source);
        assertEquals("AA", source.getPeriodCode());
    }

    @Test
    public void changeDatingMethod(){
        createAndSaveTypeTranslation("DatingMethod", "geological", "geol");
        DatesPeriod source = new DatesPeriod();
        source.setDatingMethod("geol");
        translationService.translateValues(source);
        assertEquals("geological", source.getDatingMethod());
    }

    @Test
    public void changeNotes(){
        createAndSaveTypeTranslation("Notes", "Spelling", "Speling");
        DatesPeriod source = new DatesPeriod();
        source.setNotes("Speling");
        translationService.translateValues(source);
        assertEquals("Spelling", source.getNotes());
    }
}
