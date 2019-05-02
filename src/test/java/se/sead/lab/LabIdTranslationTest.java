package se.sead.lab;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = LabIdTranslationTest.Config.class)
@Transactional
public class LabIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    private static final String ID = "ID";

    @Test
    public void changeLabname(){
        createAndSaveIdTranslation("Labname", "Correct name" );
        BugsLab source = create();
        source.setLabName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getLabName());
    }

    private void createAndSaveIdTranslation(
        String column,
        String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TLab", ID, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    private BugsLab create(){
        BugsLab source = new BugsLab();
        source.setLabId(ID);
        return source;
    }

    @Test
    public void changeAddress(){
        createAndSaveIdTranslation("Address", "Correct name");
        BugsLab source = create();
        source.setAddress("Wrong address");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getAddress());
    }

    @Test
    public void changeCountry(){
        createAndSaveIdTranslation("Country", "Correct name");
        BugsLab source = create();
        source.setCountry("Wrong address");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getCountry());
    }

    @Test
    public void changeTelephone(){
        createAndSaveIdTranslation("Telephone", "Correct name");
        BugsLab source = create();
        source.setTelephone("Wrong address");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getTelephone());
    }

    @Test
    public void changeWebsite(){
        createAndSaveIdTranslation("Website", "Correct name");
        BugsLab source = create();
        source.setWebsite("Wrong address");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getWebsite());
    }

    @Test
    public void chnageEmail(){
        createAndSaveIdTranslation("email", "Correct name");
        BugsLab source = create();
        source.setEmail("Wrong address");
        translationService.translateValues(source);
        assertEquals("Correct name", source.getEmail());
    }

}
