package se.sead.site;

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
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = SiteTypeTranslationTest.Config.class)
@Transactional
public class SiteTypeTranslationTest {
    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeSiteName(){
        createAndSaveTypeTranslation("SiteName", "Wrong", "Correct");
        BugsSite source = new BugsSite();
        source.setName("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getName());
    }

    private void createAndSaveTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TSite", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeRegion(){
        createAndSaveTypeTranslation("Region", "Wrong", "Correct");
        BugsSite source = new BugsSite();
        source.setRegion("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getRegion());
    }

    @Test
    public void changeCountry(){
        createAndSaveTypeTranslation("Country", "Wrong", "Correct");
        BugsSite source = new BugsSite();
        source.setCountry("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getCountry());
    }

    @Test
    public void changeNGR(){
        createAndSaveTypeTranslation("NGR", "Wrong", "Correct");
        BugsSite source = new BugsSite();
        source.setNgr("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getNgr());
    }

    @Test
    public void changeLatDD(){
        createAndSaveTypeTranslation("LatDD", "0", "11.000000");
        BugsSite source = new BugsSite();
        source.setLatDD(0f);
        translationService.translateValues(source);
        assertEquals(Float.valueOf(11f), source.getLatDD());
    }

    @Test
    public void changeLongDD(){
        createAndSaveTypeTranslation("LongDD", "0", "11.000000");
        BugsSite source = new BugsSite();
        source.setLongDD(0f);
        translationService.translateValues(source);
        assertEquals(Float.valueOf(11f), source.getLongDD());
    }

    @Test
    public void changeAlt(){
        createAndSaveTypeTranslation("Alt", "0", "-1");
        BugsSite source = new BugsSite();
        source.setAlt(0f);
        translationService.translateValues(source);
        assertEquals(Float.valueOf(-1f), source.getAlt());
    }

    @Test
    public void changeIDBy(){
        createAndSaveTypeTranslation("IDBy", "Wrong", "Correct");
        BugsSite source = new BugsSite();
        source.setIDBy("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getIDBy());
    }

    @Test
    public void changeInterp(){
        createAndSaveTypeTranslation("Interp", "Wrong", "Correct");
        BugsSite source = new BugsSite();
        source.setInterp("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getInterp());
    }

    @Test
    public void changeSpecimen(){
        createAndSaveTypeTranslation("Specimens", "Wrong", "Correct");
        BugsSite source = new BugsSite();
        source.setSpecimens("Wrong");
        translationService.translateValues(source);
        assertEquals("Correct", source.getSpecimens());
    }
}
