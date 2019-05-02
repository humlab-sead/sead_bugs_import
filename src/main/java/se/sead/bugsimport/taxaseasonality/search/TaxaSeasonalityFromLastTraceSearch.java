package se.sead.bugsimport.taxaseasonality.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.converters.SeasonActiveTraceHelper;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;

@Component
@Order(1)
public class TaxaSeasonalityFromLastTraceSearch implements SeasonalitySearchRule {

    @Autowired
    private SeasonActiveTraceHelper traceHelper;
    private TaxaSeasonality found;

    @Override
    public void init() {
        found = null;
    }

    @Override
    public boolean findFor(SeasonActiveAdult bugsData) {
        found = traceHelper.getFromLastTrace(bugsData.compressToString());
        return found != null;
    }

    @Override
    public TaxaSeasonality getFound() {
        return found;
    }
}
