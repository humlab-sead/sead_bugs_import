package se.sead.ecocodedefinition.koch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = EcoDefKochIdTranslationTest.Config.class)
public class EcoDefKochIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String DEFAULT_ID = "Ecoab";

    @Test
    public void changeKochCode(){
        createAndSaveIdTranslation("KochCode", "ab");
        EcoDefKoch source = create();
        source.setKochCode("AB");
        translationService.translateValues(source);
        assertEquals("ab", source.getKochCode());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TEcoDefKoch", DEFAULT_ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private EcoDefKoch create(){
        EcoDefKoch source = new EcoDefKoch();
        source.setBugsKochCode(DEFAULT_ID);
        return source;
    }

    @Test
    public void changeFullName(){
        createAndSaveIdTranslation("FullName", "Correct name");
        EcoDefKoch source = create();
        source.setFullName("Whatever");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getFullName());
    }

    @Test
    public void changeKochGroup(){
        createAndSaveIdTranslation("KochGroup", "Correct name");
        EcoDefKoch source = create();
        source.setKochGroup("Whatever");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getKochGroup());
    }

    @Test
    public void changeDescription(){
        createAndSaveIdTranslation("Description", "Correct name");
        EcoDefKoch source = create();
        source.setDescription("Whatever");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getDescription());
    }

    @Test
    public void changeNotes(){
        createAndSaveIdTranslation("Notes", "Correct name");
        EcoDefKoch source = create();
        source.setNotes("Whatever");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getNotes());
    }
}
