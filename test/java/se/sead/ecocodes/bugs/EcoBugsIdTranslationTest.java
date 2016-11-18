package se.sead.ecocodes.bugs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = EcoBugsIdTranslationTest.Config.class)
@Transactional
public class EcoBugsIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCode(){
        createAndSaveIdTranslation(
                "{1.0,Eco}",
                "CODE",
                "10.0"
        );
        EcoBugs source = new EcoBugs();
        source.setCode(1d);
        source.setBugsEcoCode("Eco");
        translationService.translateValues(source);
        assertEquals(new Double(10), source.getCode());
    }

    private void createAndSaveIdTranslation(
            String bugsDefinition,
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TEcoBugs", bugsDefinition, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeBugsEcoCode(){
        createAndSaveIdTranslation(
                "{1.0,beco}",
                "BugsEcoCODE",
                "BEco"
        );
        EcoBugs source = new EcoBugs();
        source.setCode(1d);
        source.setBugsEcoCode("beco");
        translationService.translateValues(source);
        assertEquals("BEco", source.getBugsEcoCode());
    }
}
