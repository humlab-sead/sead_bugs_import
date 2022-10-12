package se.sead.species;

import org.junit.Before;
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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SpeciesIdTranslationTest.Config.class)
@Transactional
public class SpeciesIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "1.0";
    private INDEX source;

    @Before
    public void setup(){
        source = new INDEX();
        source.setCode(1d);
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
        IdBasedTranslation translation = IdTranslationBuilder.create("INDEX", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeFamily(){
        createAndSaveIdTranslation("FAMILY", "Corrected");
        source.setFamily("Wrong");
        translationService.translateValues(source);
        assertEquals("Corrected", source.getFamily());
    }

    @Test
    public void changeGenus(){
        createAndSaveIdTranslation("GENUS", "Corrected");
        source.setGenus("Wrong");
        translationService.translateValues(source);
        assertEquals("Corrected", source.getGenus());
    }

    @Test
    public void changeSpecies(){
        createAndSaveIdTranslation("SPECIES", "Corrected");
        source.setSpecies("Wrong");
        translationService.translateValues(source);
        assertEquals("Corrected", source.getSpecies());
    }

    @Test
    public void changeAuthority(){
        createAndSaveIdTranslation("AUTHORITY", "Corrected");
        source.setAuthority("Wrong");
        translationService.translateValues(source);
        assertEquals("Corrected", source.getAuthority());
    }
}
