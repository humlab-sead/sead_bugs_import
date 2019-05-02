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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = EcoKochIdTranslationTest.Config.class)
@Transactional
public class EcoKochIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation(
                "{1.0,eco}",
                "CODE",
                "10"
        );
        EcoKoch source = new EcoKoch();
        source.setCode(1d);
        source.setBugsKochCode("eco");
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getCode());
    }

    private void createAndSaveIdTranslation(
            String identifier,
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TEcoKoch", identifier, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeBugsKochCode(){
        createAndSaveIdTranslation(
                "{1.0,eco}",
                "BugsKochCode",
                "Eco"
        );
        EcoKoch source = new EcoKoch();
        source.setCode(1d);
        source.setBugsKochCode("eco");
        translationService.translateValues(source);
        assertEquals("Eco", source.getBugsKochCode());
    }
}
