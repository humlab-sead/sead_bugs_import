package se.sead.attributes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;
import se.sead.translations.utils.TypeTranslationBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AttributeTypeTranslationTest.Config.class)
@ActiveProfiles("test")
@Transactional
public class AttributeTypeTranslationTest {

    @TestConfiguration
    static class Config extends NoAccessFileOnlyDataModelConfig{}

    @Autowired
    private TypeTranslationRepository translationRepository;
    @Autowired
    private BugsValueTranslationService translationService;

    @Test
    public void changeCODE(){
        createAndSaveTypeTranslation("CODE", "1.0000", "CODE", "99.000");
        BugsAttributes source = new BugsAttributes();
        source.setCode(99d);
        translationService.translateValues(source);
        assertEquals(Double.valueOf(1d), source.getCode());
    }

    private void createAndSaveTypeTranslation(
            String sourceColumn,
            String replacementValue,
            String targetColumn,
            String triggeringColumnValue){
        TypeTranslation translation = TypeTranslationBuilder.create("TAttributes", sourceColumn, replacementValue, targetColumn, triggeringColumnValue);
        translationRepository.saveOrUpdate(translation);
    }

    @Test
    public void changeType(){
        createAndSaveTypeTranslation("AttribType", "Type", "AttribType", "T");
        BugsAttributes source = new BugsAttributes();
        source.setType("T");
        translationService.translateValues(source);
        assertEquals("Type", source.getType());
    }

    @Test
    public void changeMeasure(){
        createAndSaveTypeTranslation("AttribMeasure", "Measure", "AttribMeasure", "measure");
        BugsAttributes source = new BugsAttributes();
        source.setMeasure("measure");
        translationService.translateValues(source);
        assertEquals("Measure", source.getMeasure());
    }

    @Test
    public void changeValue(){
        createAndSaveTypeTranslation("Value", "123", "Value", "1");
        BugsAttributes source = new BugsAttributes();
        source.setValue(1f);
        translationService.translateValues(source);
        assertEquals(Float.valueOf(123f), source.getValue());
    }

    @Test
    public void changeUnits(){
        createAndSaveTypeTranslation("AttribUnits", "unit", "AttribUnits", "NaN");
        BugsAttributes source = new BugsAttributes();
        source.setUnits("NaN");
        translationService.translateValues(source);
        assertEquals("unit", source.getUnits());
    }

}
