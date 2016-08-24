package se.sead.bugsimport.taxaseasonality.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdultBugsTable;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.TaxaSeasonalityRepository;

@Component
public class SeasonActiveTraceHelper extends SeadDataFromTraceHelper<SeasonActiveAdult, TaxaSeasonality> {

    @Autowired
    public SeasonActiveTraceHelper(TaxaSeasonalityRepository seasonalityRepository) {
        super(SeasonActiveAdultBugsTable.TABLE_NAME, true, seasonalityRepository);
    }
}
