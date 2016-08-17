package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.tracing.seadmodel.BugsError;

import java.util.List;

public interface BugsErrorRepository extends Repository<BugsError, Integer>{
    List<BugsError> findByBugsTableAndCompressedBugsData(String bugsTable, String compressedBugsData);
    List<BugsError> findByBugsTableAndBugsIdentifier(String bugsTable, String bugsIdentifier);
    BugsError save(BugsError error);
}
