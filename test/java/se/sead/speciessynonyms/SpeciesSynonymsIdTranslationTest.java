package se.sead.speciessynonyms;

import org.junit.Before;
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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SpeciesSynonymsIdTranslationTest.Config.class)
@Transactional
public class SpeciesSynonymsIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "{1.0,SynGenus,SynSpecies,SynAuthority,Ref,Notes}";
    private Synonym source;

    @Before
    public void setup(){
        source = new Synonym();
        source.setCode(1d);
        source.setSynGenus("SynGenus");
        source.setSynSpecies("SynSpecies");
        source.setSynAuthority("SynAuthority");
        source.setReference("Ref");
        source.setNotes("Notes");
    }

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation("CODE", "10");
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getCode());
    }

    private void createAndSaveIdTranslation(String column, String replacementValue){
        IdBasedTranslation translation = IdTranslationBuilder.create("TSynonym", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeSynGenus(){
        createAndSaveIdTranslation("SynGenus", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSynGenus());
    }

    @Test
    public void changeSynSpecies(){
        createAndSaveIdTranslation("SynSpecies", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSynSpecies());
    }

    @Test
    public void changeSynAuthority(){
        createAndSaveIdTranslation("SynAuthority", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSynAuthority());
    }

    @Test
    public void changeRef(){
        createAndSaveIdTranslation("Ref", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getReference());
    }

    @Test
    public void changeNotes(){
        createAndSaveIdTranslation("Notes", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getNotes());
    }
}
