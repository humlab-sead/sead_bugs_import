package se.sead.bugsimport.taxaseasonality.search;

import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;

public interface SeasonalitySearchRule {
    void init();
    boolean findFor(SeasonActiveAdult bugsData);
    TaxaSeasonality getFound();
}
