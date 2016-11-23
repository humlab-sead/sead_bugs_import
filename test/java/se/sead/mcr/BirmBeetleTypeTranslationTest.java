package se.sead.mcr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = BirmBeetleTypeTranslationTest.Config.class)
@Transactional
public class BirmBeetleTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1", "10");
        BirmBeetleDat source = new BirmBeetleDat();
        source.setBugsCode(1d);
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getBugsCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TbirmBEETLEdat", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeRow(){
        createAndSaveTypeTranslation("MCRRow", "1", "10");
        BirmBeetleDat source = new BirmBeetleDat();
        source.setRow((short)1);
        translationService.translateValues(source);
        assertEquals(new Short("10"), source.getRow());
    }

    @Test(expected = Exception.class)
    public void cannotChangeFieldX(){
        createAndSaveTypeTranslation("Field1", "1", "10");
        BirmBeetleDat source = new BirmBeetleDat();
        source.setFieldData(1, "1");
        translationService.translateValues(source);
    }
}
