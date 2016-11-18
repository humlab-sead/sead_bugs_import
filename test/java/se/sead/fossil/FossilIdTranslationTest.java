package se.sead.fossil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = FossilIdTranslationTest.Config.class)
@Transactional
public class FossilIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String FOSSIL_CODE = "FOSS00001";

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation("CODE", "1");
        Fossil source = create();
        source.setCode(10d);
        translationService.translateValues(source);
        assertEquals(new Double(1d), source.getCode());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TFossil", FOSSIL_CODE, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private Fossil create(){
        Fossil source = new Fossil();
        source.setFossilBugsCODE(FOSSIL_CODE);
        return source;
    }

    @Test
    public void changeSampleCode(){
        createAndSaveIdTranslation("SampleCODE", "SAMP0001");
        Fossil source = create();
        source.setSampleCODE("SAMP");
        translationService.translateValues(source);
        assertEquals("SAMP0001", source.getSampleCODE());
    }

    @Test
    public void changeAbundance(){
        createAndSaveIdTranslation("Abundance", "1");
        Fossil source = create();
        source.setAbundance(0);
        translationService.translateValues(source);
        assertEquals(new Integer(1), source.getAbundance());
    }
}
