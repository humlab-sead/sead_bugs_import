package se.sead.fossil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = FossilTypeTranslationTest.Config.class)
@Transactional
public class FossilTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1", "10");
        Fossil source = new Fossil();
        source.setCode(1d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TFossil", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeSampleCODE(){
        createAndSaveTypeTranslation("SampleCODE", "SAMP", "SAMP0001");
        Fossil source = new Fossil();
        source.setSampleCODE("SAMP");
        translationService.translateValues(source);
        assertEquals("SAMP0001", source.getSampleCODE());
    }

    @Test
    public void changeAbundance(){
        createAndSaveTypeTranslation("Abundance", "1", "0");
        Fossil source = new Fossil();
        source.setAbundance(1);
        translationService.translateValues(source);
        assertEquals(Integer.valueOf(0), source.getAbundance());
    }
}
