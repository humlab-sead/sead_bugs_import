package se.sead.bugsimport.attributes.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.attributes.bugsmodel.AttributesBugsTable;
import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.MeasuredAttributesRepository;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.errorlog.SingleMessageErrorLog;

import java.util.List;

import static se.sead.bugsimport.tracing.SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT;
import static se.sead.bugsimport.tracing.SeadDataFromTraceHelper.seadDataExistsAndHasBeenEditedSinceImport;

@Component
@Order(2)
public class TaxaMeasuredAttributesTraceCheck implements TaxaMeasuredAttributesSearch{

    private static final TaxaMeasuredAttributes SEAD_DATA_CONTAIN_NEWER_UPDATES;

    static {
        SEAD_DATA_CONTAIN_NEWER_UPDATES = new TaxaMeasuredAttributes();
        SEAD_DATA_CONTAIN_NEWER_UPDATES.addError(new SingleMessageErrorLog(SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT));
    }

    @Autowired
    private TaxaMeasuredAttributesBySpeciesTraceHelper traceHelper;

    @Override
    public TaxaMeasuredAttributes findFor(BugsAttributes bugsData) {
        if(traceHelper.seadSpeciesDataUpdatedAfterLastImport(bugsData)){
            return SEAD_DATA_CONTAIN_NEWER_UPDATES;
        }
        return NO_ATTRIBUTES_FOUND;
    }

    @Component
    public static class TaxaMeasuredAttributesBySpeciesTraceHelper  {

        @Autowired
        private BugsTraceRepository traceRepository;
        @Autowired
        private TaxonomicOrderRepository taxonomicOrderRepository;
        @Autowired
        private MeasuredAttributesRepository measuredAttributesRepository;

        boolean seadSpeciesDataUpdatedAfterLastImport(BugsAttributes bugsData){
            BugsTrace latestTrace = getLatestTrace(bugsData);
            TaxaSpecies species = getSpecies(bugsData);
            return anyMeasuredAttributesUpdatedAfterLastImport(species, latestTrace);
        }

        private BugsTrace getLatestTrace(BugsAttributes bugsData){
            List<BugsTrace> tracesForSpecies = traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(AttributesBugsTable.TABLE_NAME, bugsData.getBugsIdentifier());
            if(tracesForSpecies.size() > 1){
                return tracesForSpecies.get(0);
            } else {
                return BugsTrace.NO_TRACE;
            }
        }

        private TaxaSpecies getSpecies(BugsAttributes bugsData){
            return taxonomicOrderRepository.findBugsSpeciesByCode(bugsData.getCode());
        }

        private boolean anyMeasuredAttributesUpdatedAfterLastImport(TaxaSpecies species, BugsTrace lastImportTrace){
            List<TaxaMeasuredAttributes> attributesForSpecies = measuredAttributesRepository.findBySpecies(species);
            return attributesForSpecies.stream()
                    .anyMatch(attribute ->
                            seadDataExistsAndHasBeenEditedSinceImport(attribute, lastImportTrace)
                    );
        }
    }
}
