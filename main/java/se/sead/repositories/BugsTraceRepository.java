package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public interface BugsTraceRepository extends Repository<BugsTrace, Integer> {
    List<BugsTrace> findByBugsTableAndCompressedBugsData(String bugsTable, String compressedBugsData);
    BugsTrace save(BugsTrace trace);
}
