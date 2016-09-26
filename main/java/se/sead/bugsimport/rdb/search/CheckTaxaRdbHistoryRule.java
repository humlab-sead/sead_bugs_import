package se.sead.bugsimport.rdb.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.RdbRepository;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.utils.BigDecimalDefinition;

import java.util.Collections;
import java.util.List;

@Component
@Order(2)
public class CheckTaxaRdbHistoryRule implements RdbSearch {

    private static final Rdb seadDataUpdatedAfterLatestTrace;

    static {
        seadDataUpdatedAfterLatestTrace = new Rdb();
        seadDataUpdatedAfterLatestTrace.addError("Sead rdb data updated after latest Bugs import.");
    }

    private RdbIdentificationTraceHelper historySearcher;

    @Autowired
    public CheckTaxaRdbHistoryRule(TaxonomicOrderRepository orderConverter, BugsTraceRepository traceRepository, RdbRepository rdbRepository){
        historySearcher = new RdbIdentificationTraceHelper(orderConverter, traceRepository, rdbRepository);
    }

    @Override
    public Rdb findFor(BugsRDB rdb) {
        if(historySearcher.seadDataNewThanLatestImportValue(rdb)){
            return seadDataUpdatedAfterLatestTrace;
        }
        return NO_RDB_FOUND;
    }

    private static class RdbIdentificationTraceHelper {

        private TaxonomicOrderRepository taxonomicOrderRepository;
        private BugsTraceRepository traceRepository;
        private RdbRepository rdbRepository;

        public RdbIdentificationTraceHelper(TaxonomicOrderRepository taxonomicOrderRepository, BugsTraceRepository traceRepository, RdbRepository rdbRepository) {
            this.taxonomicOrderRepository = taxonomicOrderRepository;
            this.traceRepository = traceRepository;
            this.rdbRepository = rdbRepository;
        }

        boolean seadDataNewThanLatestImportValue(BugsRDB bugsRDB){
            List<Rdb> seadSpeciesRdbs = getSpeciesRdbs(bugsRDB);
            BugsTrace latestTrace = latestTraceForSpecies(bugsRDB);
            return anySeadDataUpdatedAfterLatestTrace(seadSpeciesRdbs, latestTrace);
        }

        private List<Rdb> getSpeciesRdbs(BugsRDB bugsRDB) {
            if(bugsRDB.getCode() == null){
                return Collections.EMPTY_LIST;
            }
            TaxaSpecies bugsSpecies = taxonomicOrderRepository.findBugsSpeciesByCode(BigDecimalDefinition.convertToSeadCode(bugsRDB.getCode()));
            if(bugsSpecies == null){
                return Collections.EMPTY_LIST;
            }
            return rdbRepository.findAllBySpecies(bugsSpecies);
        }

        private BugsTrace latestTraceForSpecies(BugsRDB bugsRDB) {
            List<BugsTrace> allTracesForSpecies = traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(bugsRDB.bugsTable(), bugsRDB.getBugsIdentifier());
            if(allTracesForSpecies.isEmpty()){
                return BugsTrace.NO_TRACE;
            } else {
                return allTracesForSpecies.get(0);
            }
        }

        private boolean anySeadDataUpdatedAfterLatestTrace(List<Rdb> seadSpeciesRdbs, BugsTrace latestTrace){
            if(latestTrace == BugsTrace.NO_TRACE){
                return false;
            }
            return seadSpeciesRdbs.stream()
                    .anyMatch(
                            seadRdb -> SeadDataFromTraceHelper.seadDataExistsAndHasBeenEditedSinceImport(seadRdb, latestTrace)
                    );
        }


    }
}
