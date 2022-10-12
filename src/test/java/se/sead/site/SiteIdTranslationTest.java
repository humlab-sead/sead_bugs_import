package se.sead.site;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SiteIdTranslationTest.Config.class)
@Transactional
public class SiteIdTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "SITE_ID";
    private BugsSite source;

    @Before
    public void setup(){
        source = new BugsSite();
        source.setCode(ID);
    }

    @Test
    public void setSiteName(){
        createAndSaveIdTranslation("SiteName", "Correct");
        source.setName("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getName());
    }

    private void createAndSaveIdTranslation(
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TSite", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeRegion(){
        createAndSaveIdTranslation("Region", "Correct");
        source.setRegion("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRegion());
    }

    @Test
    public void changeCountry(){
        createAndSaveIdTranslation("Country", "Correct");
        source.setCountry("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountry());
    }

    @Test
    public void changeNGR(){
        createAndSaveIdTranslation("NGR", "Correct");
        source.setNgr("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getNgr());
    }

    @Test
    public void changeLatDD(){
        createAndSaveIdTranslation("LatDD", "11.1");
        source.setLatDD(0f);
        translationService.translateValues(source);
        assertEquals(Float.valueOf(11.1f), source.getLatDD());
    }

    @Test
    public void changeLongDD(){
        createAndSaveIdTranslation("LongDD", "11.1");
        source.setLongDD(0f);
        translationService.translateValues(source);
        assertEquals(Float.valueOf(11.1f), source.getLongDD());
    }

    @Test
    public void changeAlt(){
        createAndSaveIdTranslation("Alt", "11.1");
        source.setAlt(0f);
        translationService.translateValues(source);
        assertEquals(Float.valueOf(11.1f), source.getAlt());
    }

    @Test
    public void changeIdBy(){
        createAndSaveIdTranslation("IDBy", "Correct");
        source.setIDBy("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getIDBy());
    }

    @Test
    public void changeInterp(){
        createAndSaveIdTranslation("Interp", "Correct");
        source.setInterp("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getInterp());
    }

    @Test
    public void changeSpecimens() {
        createAndSaveIdTranslation("Specimens", "Correct");
        source.setSpecimens("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSpecimens());
    }
}
