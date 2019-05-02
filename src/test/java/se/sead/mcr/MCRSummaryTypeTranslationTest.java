package se.sead.mcr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = MCRSummaryTypeTranslationTest.Config.class)
@Transactional
public class MCRSummaryTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCode(){
        createAndSaveTypeTranslation("CODE", "1", "10");
        MCRSummaryData source = new MCRSummaryData();
        source.setCode(1d);
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TMCRSummaryData", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeMaxLo(){
        createAndSaveTypeTranslation("TMaxLo", "10", "15");
        MCRSummaryData source = new MCRSummaryData();
        source.setMaxLo((short)10);
        translationService.translateValues(source);
        assertEquals(new Short("15"), source.getMaxLo());
    }

    @Test
    public void changeMaxHi(){
        createAndSaveTypeTranslation("TMaxHi", "10", "15");
        MCRSummaryData source = new MCRSummaryData();
        source.setMaxHi((short)10);
        translationService.translateValues(source);
        assertEquals(new Short("15"), source.getMaxHi());
    }

    @Test
    public void changeMinLo(){
        createAndSaveTypeTranslation("TMinLo", "10", "15");
        MCRSummaryData source = new MCRSummaryData();
        source.setMinLo((short)10);
        translationService.translateValues(source);
        assertEquals(new Short("15"), source.getMinLo());
    }

    @Test
    public void changeMinHi(){
        createAndSaveTypeTranslation("TMinHi", "10", "15");
        MCRSummaryData source = new MCRSummaryData();
        source.setMinHi((short)10);
        translationService.translateValues(source);
        assertEquals(new Short("15"), source.getMinHi());
    }

    @Test
    public void changeRangeLo(){
        createAndSaveTypeTranslation("TRangeLo", "10", "15");
        MCRSummaryData source = new MCRSummaryData();
        source.setRangeLo((short)10);
        translationService.translateValues(source);
        assertEquals(new Short("15"), source.getRangeLo());
    }

    @Test
    public void changeRangeHi(){
        createAndSaveTypeTranslation("TRangeHi", "10", "15");
        MCRSummaryData source = new MCRSummaryData();
        source.setRangeHi((short)10);
        translationService.translateValues(source);
        assertEquals(new Short("15"), source.getRangeHi());
    }

    @Test
    public void changeCogmidMax(){
        createAndSaveTypeTranslation("COGMidTMax", "10", "15");
        MCRSummaryData source = new MCRSummaryData();
        source.setCogMidTMax((short)10);
        translationService.translateValues(source);
        assertEquals(new Short("15"), source.getCogMidTMax());
    }

    @Test
    public void changeCogMidRange(){
        createAndSaveTypeTranslation("COGMidTRange", "10", "15");
        MCRSummaryData source = new MCRSummaryData();
        source.setCogMidTRange((short)10);
        translationService.translateValues(source);
        assertEquals(new Short("15"), source.getCogMidTRange());
    }
}
