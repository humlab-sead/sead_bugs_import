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
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=LabTypeTranslationTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class LabTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeLabName(){
        createAndChangeTypeTranslation("Labname", "Wrong name", "Correct Name");
        BugsLab source = new BugsLab();
        source.setLabName("Wrong name");
        translationService.translateValues(source);
        assertEquals("Correct Name", source.getLabName());
    }

    private void createAndChangeTypeTranslation(
            String column,
            String triggerValue,
            String replacementValue
    ){
        TypeTranslation translation = TypeTranslationBuilder.create("TLab", column, replacementValue, column, triggerValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeAddress(){
        createAndChangeTypeTranslation("Address", "Wrong address", "Correct address");
        BugsLab source = new BugsLab();
        source.setAddress("Wrong address");
        translationService.translateValues(source);
        assertEquals("Correct address", source.getAddress());
    }

    @Test
    public void changeCountry(){
        createAndChangeTypeTranslation("Country", "Wrong country", "Correct country");
        BugsLab source = new BugsLab();
        source.setCountry("Wrong country");
        translationService.translateValues(source);
        assertEquals("Correct country", source.getCountry());
    }

    @Test
    public void changeTelephone(){
        createAndChangeTypeTranslation("Telephone", "Wrong number", "Correct number");
        BugsLab source = new BugsLab();
        source.setTelephone("Wrong number");
        translationService.translateValues(source);
        assertEquals("Correct number", source.getTelephone());
    }

    @Test
    public void changeWebsite(){
        createAndChangeTypeTranslation("Website", "Wrong site", "Correct site");
        BugsLab source = new BugsLab();
        source.setWebsite("Wrong site");
        translationService.translateValues(source);
        assertEquals("Correct site", source.getWebsite());
    }

    @Test
    public void changeEmail(){
        createAndChangeTypeTranslation("email", "Wrong address", "Correct address");
        BugsLab source = new BugsLab();
        source.setEmail("Wrong address");
        translationService.translateValues(source);
        assertEquals("Correct address", source.getEmail());
    }

}
