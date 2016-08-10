package se.sead.sead.recordtypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.repositories.RecordTypeRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class RecordTypeCache {

    @Autowired
    private RecordTypeRepository repository;

    private Map<String, RecordType> cache;

    public RecordTypeCache(){
        cache = new HashMap<>();
    }

    public RecordType getByName(String typeName){
        if(cache.containsKey(typeName)){
            return cache.get(typeName);
        } else {
            return readFromDbAndCache(typeName);
        }
    }

    private RecordType readFromDbAndCache(String typeName) {
        RecordType fromDb = repository.findByName(typeName);
        if(fromDb == null){
            throw new IllegalStateException("No record type found for name: " + typeName);
        }
        cache.put(typeName, fromDb);
        return fromDb;
    }
}
