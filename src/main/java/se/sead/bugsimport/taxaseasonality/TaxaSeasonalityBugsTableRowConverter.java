package se.sead.bugsimport.taxaseasonality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.bugsimport.taxaseasonality.search.SeasonalitySearchRule;

import java.util.List;

@Component
public class TaxaSeasonalityBugsTableRowConverter implements BugsTableRowConverter<SeasonActiveAdult, TaxaSeasonality> {

    @Autowired
    private List<SeasonalitySearchRule> searchRules;

    @Override
    public TaxaSeasonality convertForDataRow(SeasonActiveAdult bugsData) {
        for (SeasonalitySearchRule searchRule :
                getSearchRules()) {
            searchRule.init();
            if(searchRule.findFor(bugsData)){
                return searchRule.getFound();
            }
        }
        throw new IllegalStateException("Non of the search methods returned any results. This should be impossible.");
    }

    protected List<SeasonalitySearchRule> getSearchRules(){
        return searchRules;
    }
}
