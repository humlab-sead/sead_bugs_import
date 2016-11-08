package se.sead.taxaseasonality.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.Application;
import se.sead.bugsimport.taxaseasonality.search.*;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class, SearchRuleOrderTest.class})
@TestConfiguration
@TestComponent
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class SearchRuleOrderTest {

    private static final int EXPECTED_LAST_TRACE_ORDER_INDEX = 0;
    private static final int EXPECTED_SEASONALITY_HISTORY_INDEX = 1;
    private static final int EXPECTED_DATABASE_SEARCH_RULE = 2;
    private static final int EXPECTED_CREATE_NEW_INDEX = 3;

    @Autowired
    private List<SeasonalitySearchRule> rules;

    @Test
    public void checkLastTracePosition(){
        Class actualClass = getActualClassAt(EXPECTED_LAST_TRACE_ORDER_INDEX);
        assertEquals(TaxaSeasonalityFromLastTraceSearch.class, actualClass);
    }

    private Class getActualClassAt(int index){
        return rules.get(index).getClass();
    }

    @Test
    public void checkHistorySearchPosition(){
        Class actualClass = getActualClassAt(EXPECTED_SEASONALITY_HISTORY_INDEX);
        assertEquals(CheckTaxaSeasonalityHistoryRule.class, actualClass);
    }

    @Test
    public void checkDatabaseSearchPosition(){
        Class actualClass = getActualClassAt(EXPECTED_DATABASE_SEARCH_RULE);
        assertEquals(SeasonalityFromDatabaseSearchRule.class, actualClass);
    }

    @Test
    public void checkCreateNewRulePosition(){
        Class actualClass = getActualClassAt(EXPECTED_CREATE_NEW_INDEX);
        assertEquals(CreateNewSeasonalityRule.class, actualClass);
    }
}