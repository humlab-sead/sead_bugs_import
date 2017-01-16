package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public interface BugsTraceRepository extends Repository<BugsTrace, Integer> {
    List<BugsTrace> findByBugsTableAndAccessInformationData(String bugsTable, String accessInformationData);
    List<BugsTrace> findByBugsTableAndAccessInformationDataOrderByChangeDate(String bugsTable, String accessInformationData);
    List<BugsTrace> findByBugsTableAndBugsIdentifierOrderByChangeDate(String bugsTable, String bugsIdentifier);
    BugsTrace save(BugsTrace trace);

    List<BugsTrace> findByBugsTableAndSeadTableAndAccessInformationDataOrderByChangeDate(String bugsTable, String seadTable, String accessInformationData);
    List<BugsTrace> findByBugsTableAndSeadTableAndBugsIdentifierOrderByChangeDate(String bugsTable, String seadTable, String bugsIdentifier);

    List<BugsTrace> findBySeadTableAndSeadIdOrderByChangeDate(String seadTable, Integer seadId);
}
