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
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = EcoBugsTypeTranslationTest.Config.class)
@Transactional
public class EcoBugsTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1.0", "10.0");
        EcoBugs source = new EcoBugs();
        source.setCode(1d);
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TEcoBugs", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);

    }

    @Test
    public void changeBugsEcoCode(){
        createAndSaveTypeTranslation("BugsEcoCODE", "beco", "BEco");
        EcoBugs source = new EcoBugs();
        source.setBugsEcoCode("beco");
        translationService.translateValues(source);
        assertEquals("BEco", source.getBugsEcoCode());
    }
}
