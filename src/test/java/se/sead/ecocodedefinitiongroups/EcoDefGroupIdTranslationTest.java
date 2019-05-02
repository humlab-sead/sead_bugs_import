package se.sead.ecocodedefinitiongroups;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = EcoDefGroupIdTranslationTest.Config.class)
@Transactional
public class EcoDefGroupIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeEcoGroupCode(){
        createAndSaveIdTranslation(
                "eco",
                "EcoGroupCode",
                "Eco"
        );
        EcoDefGroups source = new EcoDefGroups();
        source.setEcoGroupCode("eco");
        source.setEcoName("Eco");
        translationService.translateValues(source);
        assertEquals("Eco", source.getEcoGroupCode());
    }

    private void createAndSaveIdTranslation(
            String definition,
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TEcoDefGroups", definition, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeEcoName(){
        createAndSaveIdTranslation("eco", "EcoName", "Correct name");
        EcoDefGroups source = new EcoDefGroups();
        source.setEcoGroupCode("eco");
        source.setEcoName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getEcoName());
    }
}
