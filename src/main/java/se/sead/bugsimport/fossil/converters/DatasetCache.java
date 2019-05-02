package se.sead.bugsimport.fossil.converters;

import org.springframework.stereotype.Component;
import se.sead.sead.data.Dataset;

import java.util.HashMap;
import java.util.Map;

@Component
public class DatasetCache {

    private Map<String, Dataset> cache;

    public DatasetCache(){
        cache = new HashMap<>();
    }

    public Dataset get(String datasetName){
        return cache.get(datasetName);
    }

    public Dataset get(Dataset dataset){
        return cache.get(dataset.getName());
    }

    public void put(Dataset dataset){
        cache.put(dataset.getName(), dataset);
    }

    public void clear() {
        cache.clear();
    }
}
