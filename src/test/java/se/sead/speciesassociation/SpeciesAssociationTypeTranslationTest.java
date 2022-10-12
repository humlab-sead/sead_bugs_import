package se.sead.speciesassociation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SpeciesAssociationTypeTranslationTest.Config.class)
@Transactional
public class SpeciesAssociationTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "10", "100");
        BugsSpeciesAssociation source = new BugsSpeciesAssociation();
        source.setCode(10d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(100d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TSpeciesAssociations", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeAssociatedSpeciesCODE(){
        createAndSaveTypeTranslation("AssociatedSpeciesCODE", "10", "100");
        BugsSpeciesAssociation source = new BugsSpeciesAssociation();
        source.setAssociatedSpeciesCODE(10d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(100), source.getAssociatedSpeciesCODE());
    }

    @Test
    public void changeAssociationType(){
        createAndSaveTypeTranslation("AssociationType", "Wrong", "Correct");
        BugsSpeciesAssociation source = new BugsSpeciesAssociation();
        source.setAssociationType("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getAssociationType());
    }

    @Test
    public void changeRef(){
        createAndSaveTypeTranslation("Ref", "Wrong", "Correct");
        BugsSpeciesAssociation source = new BugsSpeciesAssociation();
        source.setRef("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRef());
    }
}
