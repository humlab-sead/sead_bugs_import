package se.sead.mcr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.IdTranslationBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = BirmBeetleIdTranslationTest.Config.class)
@Transactional
public class BirmBeetleIdTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private IdBasedTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveIdTranslation(createIdentifier(), "CODE", "10");
        BirmBeetleDat source = createDefaultSource();
        translationService.translateValues(source);
        assertEquals(new Double(10), source.getBugsCode());
    }

    private String createIdentifier(){
        return "{1.0,1,000000000000000000000000000000000000000000000000000000000000}";
    }

    private BirmBeetleDat createDefaultSource() {
        BirmBeetleDat source = new BirmBeetleDat();
        source.setBugsCode(1d);
        source.setRow((short)1);
        IntStream.rangeClosed(1,60).forEach(field -> source.setFieldData(field, "0"));
        return source;
    }

    private void createAndSaveIdTranslation(
            String identifier,
            String column,
            String replacementValue
    ){
        IdBasedTranslation translation = IdTranslationBuilder.create("TbirmBEETLEdat", identifier, column, replacementValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeRow(){
        createAndSaveIdTranslation(createIdentifier(), "MCRRow", "2");
        BirmBeetleDat source = createDefaultSource();
        translationService.translateValues(source);
        assertEquals(new Short("2"), source.getRow());
    }

    @Test
    public void changeFieldX(){
        createAndSaveIdTranslation(createIdentifier(), "Field1", "1");
        BirmBeetleDat source = createDefaultSource();
        String fieldValues = source.compressFieldValues();
        assertEquals("000000000000000000000000000000000000000000000000000000000000", fieldValues);
    }
}
