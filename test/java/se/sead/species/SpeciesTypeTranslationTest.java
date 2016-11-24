package se.sead.species;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SpeciesTypeTranslationTest.Config.class)
@Transactional
public class SpeciesTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "10", "100");
        INDEX source = new INDEX();
        source.setCode(10d);
        translationService.translateValues(source);
        assertEquals(new Double(100d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("INDEX", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeFamily(){
        createAndSaveTypeTranslation("FAMILY", "Wrong", "Correct");
        INDEX source = new INDEX();
        source.setFamily("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getFamily());
    }

    @Test
    public void changeGenus(){
        createAndSaveTypeTranslation("GENUS", "Wrong", "Correct");
        INDEX source = new INDEX();
        source.setGenus("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getGenus());
    }

    @Test
    public void changeSpecies(){
        createAndSaveTypeTranslation("SPECIES", "Wrong", "Correct");
        INDEX source = new INDEX();
        source.setSpecies("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSpecies());
    }

    @Test
    public void changeAuthority(){
        createAndSaveTypeTranslation("AUTHORITY", "Wrong", "Correct");
        INDEX source = new INDEX();
        source.setAuthority("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getAuthority());
    }


}
