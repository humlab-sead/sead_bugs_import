package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public interface BugsTraceRepository extends Repository<BugsTrace, Integer> {
    List<BugsTrace> findByBugsTableAndCompressedBugsData(String bugsTable, String compressedBugsData);
    List<BugsTrace> findByBugsTableAndCompressedBugsDataOrderByChangeDate(String bugsTable, String compressedBugsData);
    List<BugsTrace> findByBugsTableAndBugsIdentifierOrderByChangeDate(String bugsTable, String bugsIdentifier);
    BugsTrace save(BugsTrace trace);

    List<BugsTrace> findByBugsTableAndSeadTableAndCompressedBugsData(String bugsTable, String seadTable, String compressedBugsData);
    List<BugsTrace> findByBugsTableAndSeadTableAndCompressedBugsDataOrderByChangeDate(String bugsTable, String seadTable, String compressedBugsData);
    List<BugsTrace> findByBugsTableAndSeadTableAndBugsIdentifierOrderByChangeDate(String bugsTable, String seadTable, String compressedBugsData);
}
