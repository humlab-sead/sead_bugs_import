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
import se.sead.bugs.BugsColumn;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SeasonActiveAdultTypeTranslationTest.Config.class)
@Transactional
public class SeasonActiveAdultTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private SeasonActiveAdult source;

    @Before
    public void setup(){
        source = new SeasonActiveAdult();
        source.setCode(1d);
    }

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1", "10");
        translationService.translateValues(source);
        assertEquals(Double.valueOf(10d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TSeasonActiveAdult", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeSeason(){
        createAndSaveTypeTranslation("HSeason", "Wrong", "Correct");
        source.setSeason("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSeason());
    }

    @Test
    public void changeCountryCode(){
        createAndSaveTypeTranslation("CountryCode", "Wrong", "Correct");
        source.setCountryCode("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountryCode());
    }
}
