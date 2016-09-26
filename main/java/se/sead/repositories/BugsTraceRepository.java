package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public interface BugsTraceRepository extends Repository<BugsTrace, Integer> {
    List<BugsTrace> findByBugsTableAndCompressedBugsData(String bugsTable, String compressedBugsData);
    List<BugsTrace> findByBugsTableAndCompressedBugsDataOrderByChangeDate(String bugsTable, String compressedBugsData);
    List<BugsTrace> findByBugsTableAndBugsIdentifierOrderByChangeDate(String bugsTable, String bugsIdentifier);
    BugsTrace save(BugsTrace trace);
}
