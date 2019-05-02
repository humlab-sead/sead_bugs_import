package se.sead.ecocodedefinition.bugs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = EcoDefBugsIdTranslationTest.Config.class)
public class EcoDefBugsIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String DEFAULT_ID = "BEco01";

    @Test
    public void changeSortOrder(){
        createAndSaveIdTranslation("SortOrder", "20");
        EcoDefBugs source = create();
        source.setSortOrder((short)1);
        translationService.translateValues(source);
        assertEquals((short)20, (short)source.getSortOrder());
    }

    private void createAndSaveIdTranslation(
        String targetColumn,
        String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TEcoDefBugs", DEFAULT_ID, targetColumn, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private EcoDefBugs create(){
        EcoDefBugs source = new EcoDefBugs();
        source.setBugsEcoCODE(DEFAULT_ID);
        return source;
    }

    @Test
    public void changeEcoLabel(){
        createAndSaveIdTranslation("EcoLabel", "Spelling");
        EcoDefBugs source = create();
        source.setEcoLabel("Speling");
        translationService.translateValues(source);
        assertEquals("Spelling", source.getEcoLabel());
    }

    @Test
    public void changeDefinition(){
        createAndSaveIdTranslation("Definition", "Updated");
        EcoDefBugs source = create();
        source.setDefinition("Whatever");
        translationService.translateValues(source);
        assertEquals("Updated", source.getDefinition());
    }

    @Test
    public void changeNotes(){
        createAndSaveIdTranslation("Notes", "Updated");
        EcoDefBugs source = create();
        source.setNotes("Whatever");
        translationService.translateValues(source);
        assertEquals("Updated", source.getNotes());
    }
}
