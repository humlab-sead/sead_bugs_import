package se.sead.bugsimport.periods.converters;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

import java.util.HashMap;
import java.util.Map;

@Component
public class RelativeAgeCache {

    private Map<String, RelativeAge> cache;

    public RelativeAgeCache(){
        cache = new HashMap<>();
    }

    public void clear(){
        cache.clear();
    }

    public RelativeAge get(String abbreviation){
        return cache.get(abbreviation);
    }

    public void put(RelativeAge relativeAge){
        cache.put(relativeAge.getAbbreviation(), relativeAge);
    }
}
