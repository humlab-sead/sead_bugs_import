package se.sead.bugsimport.fossil.converters;

import se.sead.sead.data.Abundance;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.Dataset;

import java.util.*;

public class AnalysisEntityCache {

    private Map<AnalysisEntityCacheKey, AnalysisEntity> cache;

    public AnalysisEntityCache(){
        cache = Collections.synchronizedMap(new HashMap<>());
    }

    void init(){
        cache.clear();
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
