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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = PeriodsIdTranslationTest.Config.class)
@Transactional
public class PeriodsIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "PERIOD_ID";

    @Test
    public void changePeriodName(){
        createAndSaveIdTranslation("PeriodName", "Correct name");
        Period source = create();
        source.setName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getName());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TPeriods", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private Period create(){
        Period source = new Period();
        source.setPeriodCode(ID);
        return source;
    }

    @Test
    public void changeType(){
        createAndSaveIdTranslation("PeriodType", "Correct name");
        Period source = create();
        source.setType("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getType());
    }

    @Test
    public void changeDesc(){
        createAndSaveIdTranslation("PeriodDesc", "Correct name");
        Period source = create();
        source.setDesc("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getDesc());
    }

    @Test
    public void changeRef(){
        createAndSaveIdTranslation("PeriodRef", "Correct name");
        Period source = create();
        source.setRef("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getRef());
    }

    @Test
    public void changeGeography(){
        createAndSaveIdTranslation("PeriodGeog", "Correct name");
        Period source = create();
        source.setGeography("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getGeography());
    }

    @Test
    public void changeBegin(){
        createAndSaveIdTranslation("Begin", "100");
        Period source = create();
        source.setBegin(1);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(100), source.getBegin());
    }

    @Test
    public void changeBeginBCAD(){
        createAndSaveIdTranslation("BeginBCAD", "Correct name");
        Period source = create();
        source.setBeginBCad("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getBeginBCad());
    }

    @Test
    public void changeEnd(){
        createAndSaveIdTranslation("End", "100");
        Period source = create();
        source.setEnd(1);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(100), source.getEnd());
    }

    @Test
    public void changeEndBCAD(){
        createAndSaveIdTranslation("EndBCAD", "Correct name");
        Period source = create();
        source.setEndBCad("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getEndBCad());
    }

    @Test
    public void changeYearsType(){
        createAndSaveIdTranslation("YearsType", "Correct name");
        Period source = create();
        source.setYearsType("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getYearsType());
    }
}
