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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = MCRSummaryIdTranslationTest.Config.class)
@Transactional
public class MCRSummaryIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCode(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "CODE", "10");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private String createIdentifer(String code){
        return "{" +
                code +
                ",10,10,10,10,10,10,10,10}";
    }

    private void createAndSaveIdTranslation(
            String identifier,
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TMCRSummaryData", identifier, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private MCRSummaryData create(Double code){
        MCRSummaryData source = new MCRSummaryData();
        source.setCode(code);
        source.setMaxLo((short)10);
        source.setMaxHi((short)10);
        source.setMinLo((short)10);
        source.setMinHi((short)10);
        source.setRangeHi((short) 10);
        source.setRangeLo((short)10);
        source.setCogMidTMax((short)10);
        source.setCogMidTRange((short)10);
        return source;
    }

    @Test
    public void changeMaxLo(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "TMaxLo", "100");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getMaxLo());
    }

    @Test
    public void changeMaxHi(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "TMaxHi", "100");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getMaxHi());
    }

    @Test
    public void changeMinLo(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "TMinLo", "100");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getMinLo());
    }

    @Test
    public void changeMinHi(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "TMinHi", "100");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getMinHi());
    }

    @Test
    public void changeRangeLo(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "TRangeLo", "100");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getRangeLo());
    }

    @Test
    public void changeRangeHi(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "TRangeHi", "100");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getRangeHi());
    }

    @Test
    public void changeCogMidMax(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "COGMidTMax", "100");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getCogMidTMax());
    }

    @Test
    public void changeCogMidRange(){
        createAndSaveIdTranslation(createIdentifer("1.0"), "COGMidTRange", "100");
        MCRSummaryData source = create(1d);
        translationService.translateValues(source);
        assertEquals(Short.valueOf("100"), source.getCogMidTRange());
    }
}
