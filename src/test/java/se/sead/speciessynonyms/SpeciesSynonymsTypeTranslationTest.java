package se.sead.speciessynonyms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SpeciesSynonymsTypeTranslationTest.Config.class)
@Transactional
public class SpeciesSynonymsTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "10", "1");
        Synonym source = new Synonym();
        source.setCode(10d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(1d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TSynonym", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeSynGenus(){
        createAndSaveTypeTranslation("SynGenus", "Wrong", "Correct");
        Synonym source = new Synonym();
        source.setSynGenus("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSynGenus());
    }

    @Test
    public void changeSynSpecies(){
        createAndSaveTypeTranslation("SynSpecies", "Wrong", "Correct");
        Synonym source = new Synonym();
        source.setSynSpecies("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSynSpecies());
    }

    @Test
    public void changeSynAuthority(){
        createAndSaveTypeTranslation("SynAuthority", "Wrong", "Correct");
        Synonym source = new Synonym();
        source.setSynAuthority("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSynAuthority());
    }

    @Test
    public void changeRef(){
        createAndSaveTypeTranslation("Ref", "Wrong", "Correct");
        Synonym source = new Synonym();
        source.setReference("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getReference());
    }

    @Test
    public void changeNotes(){
        createAndSaveTypeTranslation("Notes", "Wrong", "Correct");
        Synonym source = new Synonym();
        source.setNotes("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getNotes());
    }
}
