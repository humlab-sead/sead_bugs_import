package se.sead.specieskeys;

import org.junit.Before;
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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SpeciesKeysIdTranslationTest.Config.class)
@Transactional
public class SpeciesKeysIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "{1.0,Ref,Data}";
    private Keys source;

    @Before
    public void setup(){
        source = new Keys();
        source.setCode(1d);
        source.setRef("Ref");
        source.setData("Data");
    }

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation("CODE", "10");
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TKeys", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeRef(){
        createAndSaveIdTranslation("Ref", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRef());
    }

    @Test
    public void changeData(){
        createAndSaveIdTranslation("Data", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getData());
    }
}
