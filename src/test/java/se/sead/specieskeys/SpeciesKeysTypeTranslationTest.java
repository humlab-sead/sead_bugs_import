package se.sead.specieskeys;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SpeciesKeysTypeTranslationTest.Config.class)
@Transactional
public class SpeciesKeysTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1", "10");
        Keys source = new Keys();
        source.setCode(1d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TKeys", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeRef(){
        createAndSaveTypeTranslation("Ref", "Wrong", "Correct");
        Keys source = new Keys();
        source.setRef("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRef());
    }

    @Test
    public void changeData(){
        createAndSaveTypeTranslation("Data", "Wrong", "Correct");
        Keys source = new Keys();
        source.setData("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getData());
    }
}
