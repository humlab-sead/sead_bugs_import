package se.sead.periods;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = PeriodTypeTranslationTest.Config.class)
@Transactional
public class PeriodTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changePeriodName(){
        createAndSaveTypeTranslation("PeriodName", "Wrong name", "Correct name");
        Period source = new Period();
        source.setName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getName());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TPeriods", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changePeriodType(){
        createAndSaveTypeTranslation("PeriodType", "Wrong name", "Correct name");
        Period source = new Period();
        source.setType("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getType());
    }

    @Test
    public void changePeriodDesc(){
        createAndSaveTypeTranslation("PeriodDesc", "Wrong name", "Correct name");
        Period source = new Period();
        source.setDesc("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getDesc());
    }

    @Test
    public void changePeriodRef(){
        createAndSaveTypeTranslation("PeriodRef", "Wrong name", "Correct name");
        Period source = new Period();
        source.setRef("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getRef());
    }

    @Test
    public void changePeriodGeog(){
        createAndSaveTypeTranslation("PeriodGeog", "Wrong name", "Correct name");
        Period source = new Period();
        source.setGeography("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getGeography());
    }

    @Test
    public void changeBegin(){
        createAndSaveTypeTranslation("Begin", "1", "100");
        Period source = new Period();
        source.setBegin(1);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(100), source.getBegin());
    }

    @Test
    public void changeBeginBCAD(){
        createAndSaveTypeTranslation("BeginBCAD", "Wrong name", "Correct name");
        Period source = new Period();
        source.setBeginBCad("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getBeginBCad());
    }

    @Test
    public void changeEnd(){
        createAndSaveTypeTranslation("End", "1", "100");
        Period source = new Period();
        source.setEnd(1);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(100), source.getEnd());
    }

    @Test
    public void changeEndBCAD(){
        createAndSaveTypeTranslation("EndBCAD", "Wrong name", "Correct name");
        Period source = new Period();
        source.setEndBCad("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getEndBCad());
    }

    @Test
    public void changeYearsType(){
        createAndSaveTypeTranslation("YearsType", "Wrong name", "Correct name");
        Period source = new Period();
        source.setYearsType("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getYearsType());
    }
}
