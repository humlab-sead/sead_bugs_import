package se.sead.ecocodes.koch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoKochTypeTranslationTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class EcoKochTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1", "10");
        EcoKoch source = new EcoKoch();
        source.setCode(1d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TEcoKoch", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeBugsKochCode(){
        createAndSaveTypeTranslation("BugsKochCode", "eco", "Eco");
        EcoKoch source = new EcoKoch();
        source.setBugsKochCode("eco");
        translationService.translateValues(source);
        assertEquals("Eco", source.getBugsKochCode());
    }
}
