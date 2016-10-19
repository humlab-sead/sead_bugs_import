package se.sead.bugsimport.fossil.converters;

import se.sead.bugsimport.fossil.seadmodel.Abundance;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.Dataset;

import java.util.*;

public class AnalysisEntityCache {

    private Map<AnalysisEntityCacheKey, AnalysisEntity> cache;
    private Map<AnalysisEntity, List<Abundance>> entityMapper;

    public AnalysisEntityCache(){
        cache = Collections.synchronizedMap(new HashMap<>());
        entityMapper = Collections.synchronizedMap(new HashMap<>());
    }

    void init(){
        cache.clear();
        entityMapper.clear();
    }

    AnalysisEntity find(Sample sample, Dataset dataset){
        AnalysisEntityCacheKey key = createKey(sample, dataset);
        return cache.get(key);
    }

    private AnalysisEntityCacheKey createKey(Sample sample, Dataset dataset) {
        return new AnalysisEntityCacheKey(sample, dataset);
    }

    void store(Sample sample, Dataset dataset, AnalysisEntity analysisEntity){
        AnalysisEntityCacheKey key = createKey(sample, dataset);
        cache.put(key, analysisEntity);
    }

    void bind(AnalysisEntity analysisEntity, Abundance abundance){
        List<Abundance> abundances = entityMapper.get(analysisEntity);
        if(abundances == null){
            abundances = new ArrayList<>();
            entityMapper.put(analysisEntity, abundances);
        }
        abundances.add(abundance);
    }

    public List<Abundance> getAbundances(AnalysisEntity analysisEntity){
        return entityMapper.get(analysisEntity);
    }

    private static class AnalysisEntityCacheKey {
        Sample sample;
        Dataset dataset;

        AnalysisEntityCacheKey(Sample sample, Dataset dataset){
            this.sample = sample;
            this.dataset = dataset;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AnalysisEntityCacheKey that = (AnalysisEntityCacheKey) o;

            if (!sample.equals(that.sample)) return false;
            return dataset.equals(that.dataset);

        }

        @Override
        public int hashCode() {
            int result = sample.hashCode();
            result = 31 * result + dataset.hashCode();
            return result;
        }
    }
}
