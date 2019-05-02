package se.sead.ecocodedefinition.bugs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = EcoDefBugsTypeTranslationTest.Config.class)
@Transactional
public class EcoDefBugsTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig {}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeSortOrder(){
        createAndSaveTypeTranslation("SortOrder", "1", "20");
        EcoDefBugs source = new EcoDefBugs();
        source.setSortOrder((short)1);
        translationService.translateValues(source);
        assertEquals((short)20, (short)source.getSortOrder());
    }

    private void createAndSaveTypeTranslation(
        String column,
        String triggerValue,
        String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TEcoDefBugs", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeDefinition(){
        createAndSaveTypeTranslation("Definition", "Wrong definition", "Correct definition");
        EcoDefBugs source = new EcoDefBugs();
        source.setDefinition("Wrong definition");
        translationService.translateValues(source);
        assertEquals("Correct definition", source.getDefinition());
    }

    @Test
    public void changeNotes(){
        createAndSaveTypeTranslation("Notes", "Wrong notes", "Correct notes");
        EcoDefBugs source = new EcoDefBugs();
        source.setNotes("Wrong notes");
        translationService.translateValues(source);
        assertEquals("Correct notes", source.getNotes());
    }

    @Test
    public void changeLabel(){
        createAndSaveTypeTranslation("EcoLabel", "Wrong label", "Correct label");
        EcoDefBugs source = new EcoDefBugs();
        source.setEcoLabel("Wrong label");
        translationService.translateValues(source);
        assertEquals("Correct label", source.getEcoLabel());
    }
}
