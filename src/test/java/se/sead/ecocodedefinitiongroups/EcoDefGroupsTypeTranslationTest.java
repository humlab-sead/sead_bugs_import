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
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoDefGroupsTypeTranslationTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class EcoDefGroupsTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeEcoGroupCode(){
        createAndSaveTypeTranslation("EcoGroupCode", "eco", "Eco");
        EcoDefGroups source = new EcoDefGroups();
        source.setEcoGroupCode("eco");
        translationService.translateValues(source);
        assertEquals("Eco", source.getEcoGroupCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TEcoDefGroups", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeEcoName(){
        createAndSaveTypeTranslation("EcoName", "Wrong name", "Correct name");
        EcoDefGroups source = new EcoDefGroups();
        source.setEcoName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getEcoName());
    }
}
