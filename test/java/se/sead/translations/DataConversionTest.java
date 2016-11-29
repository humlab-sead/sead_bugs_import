package se.sead.translations;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.Application;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.testutils.NoAccessFileOnlyDataModelConfig;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, DataConversionTest.Config.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DataConversionTest {

    @TestConfiguration
    public static class Config extends NoAccessFileOnlyDataModelConfig{
    }

    @Autowired
    private BugsValueTranslationService translationEngine;

    @Autowired
    private IdBasedTranslationRepository idBasedTranslationRepository;
    @Autowired
    private TypeTranslationRepository typeTranslationRepository;

    @Test
    public void idBasedTranslationNoTranslations(){
        TraceableBugsDataImpl bugsDataToTranslate = createTraceableBugsData(1, "wrongContent", BigDecimal.ONE);
        translationEngine.translateValues(bugsDataToTranslate);
        assertEquals("wrongContent", bugsDataToTranslate.getColumnValue());
        assertEquals(BigDecimal.ONE, bugsDataToTranslate.getNumericValue());
    }

    private TraceableBugsDataImpl createTraceableBugsData(Integer id, String columnValue, BigDecimal numericValue){
        return createTraceableBugsData(id, columnValue, numericValue, false);
    }

    private TraceableBugsDataImpl createTraceableBugsData(Integer id, String columnValue, BigDecimal numericValue, boolean exportIdentifier){
        TraceableBugsDataImpl bugsData = new TraceableBugsDataImpl(exportIdentifier);
        bugsData.setId(id);
        bugsData.setColumnValue(columnValue);
        bugsData.setNumericValue(numericValue);
        return bugsData;
    }

    private IdBasedTranslation createIdBasedTranslation(TraceableBugsData traceable, String targetColumn, String newValue){
        return createIdBasedTranslation(traceable, targetColumn, newValue, false);
    }

    private IdBasedTranslation createIdBasedTranslation(TraceableBugsData traceable, String targetColumn, String newValue, boolean useIdentifierField){
        IdBasedTranslation translation = new IdBasedTranslation();
        if(useIdentifierField){
            translation.setBugsDefinition(traceable.getBugsIdentifier());
        } else {
            translation.setBugsDefinition(traceable.getCompressedStringBeforeTranslation());
        }
        translation.setBugsTable(traceable.bugsTable());
        translation.setTargetColumn(targetColumn);
        translation.setReplacementValue(newValue);
        return translation;
    }

    @Test
    public void idBasedTranslation(){
        TraceableBugsDataImpl bugsDataToTranslate = createTraceableBugsData(1, "wrongContent", BigDecimal.ONE);
        IdBasedTranslation translation = createIdBasedTranslation(bugsDataToTranslate, "columnValue", "rightContent");
        idBasedTranslationRepository.saveOrUpdate(translation);
        translationEngine.translateValues(bugsDataToTranslate);
        assertEquals("rightContent", bugsDataToTranslate.getColumnValue());
        assertEquals(BigDecimal.ONE, bugsDataToTranslate.getNumericValue());
    }

    @Test
    public void idBasedTranslationBigDecimalTarget(){
        TraceableBugsDataImpl bugsDataToTranslate  = createTraceableBugsData(1, "", BigDecimal.ONE);
        IdBasedTranslation translation = createIdBasedTranslation(bugsDataToTranslate, "numericValue", "0.00");
        idBasedTranslationRepository.saveOrUpdate(translation);
        translationEngine.translateValues(bugsDataToTranslate);
        assertTrue( BigDecimal.ZERO.compareTo(bugsDataToTranslate.getNumericValue()) == 0);
    }

    @Test
    public void idBasedTranslationWithIdentifier(){
        TraceableBugsDataImpl bugsData = createTraceableBugsData(1, "changeThis", BigDecimal.TEN, true);
        IdBasedTranslation idBaseTranslation = createIdBasedTranslation(bugsData, "columnValue", "toThis", true);
        idBasedTranslationRepository.saveOrUpdate(idBaseTranslation);
        translationEngine.translateValues(bugsData);
        assertEquals("toThis", bugsData.getColumnValue());
    }

    @Test
    public void typeBasedTranslation(){
        TraceableBugsDataImpl bugsDataToTranslation = createTraceableBugsData(1, "wrongContent", BigDecimal.ONE);
        TypeTranslation translation = createTypeTranslation(bugsDataToTranslation.bugsTable(), "columnValue", "wrongContent", "columnValue", "rightContent");
        typeTranslationRepository.saveOrUpdate(translation);
        translationEngine.translateValues(bugsDataToTranslation);
        assertEquals("rightContent", bugsDataToTranslation.getColumnValue());
    }

    private TypeTranslation createTypeTranslation(String bugsTable, String signalingColumn,  String signalingValue, String targetColumnName, String replacementValue) {
        TypeTranslation translation = new TypeTranslation();
        translation.setBugsTable(bugsTable);
        translation.setTargetColumn(targetColumnName);
        translation.setReplacementValue(replacementValue);
        translation.setTriggeringColumnValue(signalingValue);
        translation.setBugsColumn(signalingColumn);
        return translation;
    }

    @Test
    public void listOfTranslationsFromTypeTranslation(){
        TypeTranslation translation = createTypeTranslation(TraceableBugsDataImpl.TEST_IMPLEMENTATION_BUGS_TABLE_NAME, "columnValue", "wrongContent", "columnValue", "rightColumn");
        typeTranslationRepository.saveOrUpdate(translation);
        List<TraceableBugsDataImpl> testMaterial = Arrays.asList(
                createTraceableBugsData(1, "wrongContent", BigDecimal.ONE),
                createTraceableBugsData(2, "wrongContent", BigDecimal.ONE),
                createTraceableBugsData(3, "wrongContent", BigDecimal.ONE)
        );
        for (TraceableBugsData bugsData :
                testMaterial) {
            translationEngine.translateValues(bugsData);
        }
        testMaterial.forEach(bugsData -> assertEquals("rightColumn", bugsData.getColumnValue()));
    }

    @Test
    public void listOfTranslationsNotAllShouldTranslate(){
        TypeTranslation translation = createTypeTranslation(TraceableBugsDataImpl.TEST_IMPLEMENTATION_BUGS_TABLE_NAME, "columnValue", "wrongContent", "columnValue", "rightContent");
        typeTranslationRepository.saveOrUpdate(translation);
        List<TraceableBugsDataImpl> testMaterial = Arrays.asList(
                createTraceableBugsData(1, "wrongContent", BigDecimal.ONE),
                createTraceableBugsData(2, "someOtherContent", BigDecimal.ONE),
                createTraceableBugsData(3, "wrongContent", BigDecimal.ONE)
        );
        for (TraceableBugsData bugsData :
                testMaterial) {
            translationEngine.translateValues(bugsData);
        }
        assertEquals("rightContent", testMaterial.get(0).getColumnValue());
        assertEquals("rightContent", testMaterial.get(2).getColumnValue());
        assertEquals("someOtherContent", testMaterial.get(1).getColumnValue());
    }

    @Test
    public void severalTranslationsDifferentTypes(){
        TypeTranslation typeTranslation = createTypeTranslation(TraceableBugsDataImpl.TEST_IMPLEMENTATION_BUGS_TABLE_NAME, "columnValue", "wrongContent", "columnValue", "rightContent");
        TraceableBugsDataImpl bugsData = createTraceableBugsData(1, "wrongContent", BigDecimal.ONE);
        IdBasedTranslation idBasedTranslation = createIdBasedTranslation(bugsData, "numericValue", "0");
        typeTranslationRepository.saveOrUpdate(typeTranslation);
        idBasedTranslationRepository.saveOrUpdate(idBasedTranslation);
        translationEngine.translateValues(bugsData);
        assertEquals(BigDecimal.ZERO, bugsData.getNumericValue());
        assertEquals("rightContent", bugsData.getColumnValue());
    }

    @Test
    public void triggerColumnDifferFromTargetColumnTranslations(){
        TypeTranslation typeTranslation = createTypeTranslation(TraceableBugsDataImpl.TEST_IMPLEMENTATION_BUGS_TABLE_NAME, "columnValue", "wrongNumericValue", "numericValue", "1");
        typeTranslationRepository.saveOrUpdate(typeTranslation);
        TraceableBugsDataImpl wrongNumericValue = createTraceableBugsData(1, "wrongNumericValue", BigDecimal.ZERO);
        translationEngine.translateValues(wrongNumericValue);
        assertEquals("wrongNumericValue", wrongNumericValue.getColumnValue());
        assertEquals(BigDecimal.ONE, wrongNumericValue.getNumericValue());
    }

    @Test
    public void typeTranslationCausingNullValue(){
        TypeTranslation typeTranslation = createTypeTranslation(TraceableBugsDataImpl.TEST_IMPLEMENTATION_BUGS_TABLE_NAME, "columnValue", "Empty", "columnValue", null);
        typeTranslationRepository.saveOrUpdate(typeTranslation);
        TraceableBugsDataImpl toBeNullValue = createTraceableBugsData(1, "Empty", BigDecimal.ONE);
        translationEngine.translateValues(toBeNullValue);
        assertEquals(BigDecimal.ONE, toBeNullValue.getNumericValue());
        assertNull(toBeNullValue.getColumnValue());
    }
}
