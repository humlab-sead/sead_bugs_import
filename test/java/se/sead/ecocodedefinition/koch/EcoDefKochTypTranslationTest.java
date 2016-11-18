package se.sead.ecocodedefinition.koch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcoDefKochTypTranslationTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class EcoDefKochTypTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeKochCode(){
        createAndSaveTypeTranslation("KochCode", "AB", "ab");
        EcoDefKoch source = new EcoDefKoch();
        source.setKochCode("AB");
        translationService.translateValues(source);
        assertEquals("ab", source.getKochCode());
    }

    private void createAndSaveTypeTranslation(
        String column,
        String triggerValue,
        String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TEcoDefKoch", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeFullName(){
        createAndSaveTypeTranslation("FullName", "Wrong name", "Correct name");
        EcoDefKoch source = new EcoDefKoch();
        source.setFullName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getFullName());
    }

    @Test
    public void changeKochGroup(){
        createAndSaveTypeTranslation("KochGroup", "Wrong name", "Correct name");
        EcoDefKoch source = new EcoDefKoch();
        source.setKochGroup("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getKochGroup());
    }

    @Test
    public void changeDescription(){
        createAndSaveTypeTranslation("Description", "Wrong name", "Correct name");
        EcoDefKoch source = new EcoDefKoch();
        source.setDescription("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getDescription());
    }

    @Test
    public void changeNotes(){
        createAndSaveTypeTranslation("Notes", "Wrong name", "Correct name");
        EcoDefKoch source = new EcoDefKoch();
        source.setNotes("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getNotes());
    }
}
