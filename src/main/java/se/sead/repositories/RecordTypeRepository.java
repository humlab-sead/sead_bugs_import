package se.sead.repositories;

import se.sead.sead.recordtypes.RecordType;

public interface RecordTypeRepository extends CreateAndReadRepository<RecordType, Integer> {
    RecordType findByName(String name);
}
