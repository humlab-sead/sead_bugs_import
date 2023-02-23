package se.sead.testutils;

import se.sead.model.TestEqualityHelper;
import se.sead.sead.model.LoggableEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseContentVerification<SeadType extends LoggableEntity> {

    public interface DatabaseContentTestDataProvider<SeadType extends LoggableEntity> {
        List<SeadType> getExpectedData();
        List<SeadType> getActualData();
        default Comparator<SeadType> getSorter(){
            return null;
        }
        default TestEqualityHelper<SeadType> getEqualityHelper(){
            return null;
        }
        default boolean useEqualityHelper(SeadType expected, SeadType actual){
            return getEqualityHelper() != null && expected.getId() == null;
        }
    }

    public DatabaseContentTestDataProvider<SeadType> contentProvider;

    public DatabaseContentVerification(DatabaseContentTestDataProvider<SeadType> contentProvider){
        this.contentProvider = contentProvider;
    }

    public void verifyDatabaseContent(){
        List<SeadType> expectedData = contentProvider.getExpectedData();
        List<SeadType> actualData = contentProvider.getActualData();
        sortIfPossible(expectedData, actualData);
        try {
            assertEquals(expectedData.size(), actualData.size());
        } catch (AssertionError ae){
            System.out.println(expectedData);
            System.out.println(actualData);
            throw ae;
        }
        TestEqualityHelper<SeadType> equalityHelper = contentProvider.getEqualityHelper();
        for (int i = 0; i < expectedData.size(); i++) {
            SeadType expected = expectedData.get(i);
            SeadType actual = actualData.get(i);
            try {
                if(contentProvider.useEqualityHelper(expected, actual)){
                    assertTrue(equalityHelper.equalsWithoutBlackListedMethods(expected, actual));
                } else {
                    assertEquals(expected, actual);
                }
            } catch (AssertionError ae){
                System.out.println(expected);
                System.out.println(actual);
                throw ae;
            }
        }
    }

    private void sortIfPossible(List<SeadType> expectedData, List<SeadType> actualData) {
        Comparator<SeadType> comparator = contentProvider.getSorter();
        if(comparator != null){
            Collections.sort(expectedData, comparator);
            Collections.sort(actualData, comparator);
        }
    }
}
