package se.sead.taxaseasonality;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SeasonActiveAdultIdTranslationTest.Config.class)
@Transactional
public class SeasonActiveAdultIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}
    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "1.0";
    private SeasonActiveAdult source;

    @Before
    public void setup(){
        source = new SeasonActiveAdult();
        source.setCode(1d);
    }

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation("CODE", "10");
        translationService.translateValues(source);
        assertEquals(new Double(10d), source.getCode());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TSeasonActiveAdult", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeSeason(){
        createAndSaveIdTranslation("HSeason", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSeason());
    }

    @Test
    public void changeCountryCode(){
        createAndSaveIdTranslation("CountryCode", "Correct");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountryCode());
    }
}
