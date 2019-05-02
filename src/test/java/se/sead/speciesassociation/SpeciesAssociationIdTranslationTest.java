package se.sead.speciesassociation;

import org.junit.Before;
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
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SpeciesAssociationIdTranslationTest.Config.class)
@Transactional
public class SpeciesAssociationIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "1";
    private BugsSpeciesAssociation source;

    @Before
    public void setup(){
        source = new BugsSpeciesAssociation();
        source.setSpeciesAssociationID(1);
    }

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation("CODE", "10");
        source.setCode(1d);
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getCode());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TSpeciesAssociations", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeAssociatedSpeciesCODE(){
        createAndSaveIdTranslation("AssociatedSpeciesCODE", "10");
        source.setAssociatedSpeciesCODE(1d);
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getAssociatedSpeciesCODE());
    }

    @Test
    public void changeAssociationType(){
        createAndSaveIdTranslation("AssociationType", "Correct");
        source.setAssociationType("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getAssociationType());
    }

    @Test
    public void changeRef(){
        createAndSaveIdTranslation("Ref", "Correct");
        source.setRef("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRef());
    }
}
