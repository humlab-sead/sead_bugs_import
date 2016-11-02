package se.sead.bugsimport.taxaseasonality.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.converters.SeasonActiveAdultSpeciesConverter;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.TaxaSeasonalityRepository;

import java.util.Collections;
import java.util.List;

@Component
@Order(2)
public class CheckTaxaSeasonalityHistoryRule implements SeasonalitySearchRule{

    private TaxaSeasonality seadDataUpdatedAfterInitialImportErrorCarrier;

    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private SeasonActiveAdultSpeciesConverter speciesConverter;
    @Autowired
    private TaxaSeasonalityRepository seasonalityRepository;

    @Override
    public void init() {
        if(seadDataUpdatedAfterInitialImportErrorCarrier == null){
            createErrorMessage();
        }
    }

    private void createErrorMessage(){
        seadDataUpdatedAfterInitialImportErrorCarrier = new TaxaSeasonality();
        seadDataUpdatedAfterInitialImportErrorCarrier.addError(SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
    }

    @Override
    public boolean findFor(SeasonActiveAdult bugsData) {
        BugsTrace latestTrace = getLatestTraceForSpecies(bugsData);
        if(latestTrace != null){
            List<TaxaSeasonality> speciesSeasonalities = getSeasonalities(bugsData);
            return anyTaxaSeasonalityUpdatedAfterLatestTrace(latestTrace, speciesSeasonalities);
        }
        return false;
    }

    private BugsTrace getLatestTraceForSpecies(SeasonActiveAdult bugsData){
        List<BugsTrace> importTracesForSpecies = traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(bugsData.bugsTable(), bugsData.getBugsIdentifier());
        if(importTracesForSpecies.isEmpty()){
            return null;
        }
        return importTracesForSpecies.get(0);
    }

    private List<TaxaSeasonality> getSeasonalities(SeasonActiveAdult bugsData){
        TaxaSpecies species = speciesConverter.getSpecies(bugsData);
        if(species.isErrorFree()){
            return seasonalityRepository.findAllBySpecies(species);
        }
        return Collections.EMPTY_LIST;
    }

    private boolean anyTaxaSeasonalityUpdatedAfterLatestTrace(BugsTrace latestTrace, List<TaxaSeasonality> seasonalities){
        for (TaxaSeasonality seasonality :
                seasonalities) {
            if (SeadDataFromTraceHelper.seadDataExistsAndHasBeenEditedSinceImport(seasonality, latestTrace)){
                return true;
            }
        }
        return false;
    }

    @Override
    public TaxaSeasonality getFound() {
        return seadDataUpdatedAfterInitialImportErrorCarrier;
    }
}
