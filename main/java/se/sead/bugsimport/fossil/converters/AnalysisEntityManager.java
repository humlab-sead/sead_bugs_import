package se.sead.bugsimport.fossil.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.seadmodel.Abundance;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.repositories.AnalysisEntityRepository;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.Dataset;
import se.sead.utils.ErrorCopier;

import java.util.List;
import java.util.Objects;

@Component
public class AnalysisEntityManager {

    @Autowired
    private SampleTracerHelper sampleTracerHelper;
    @Autowired
    private AnalysisEntityRepository aeRepository;
    @Autowired
    private DatasetManager datasetManager;

    private AnalysisEntityCache cache = new AnalysisEntityCache();

    public void initCache(){
        cache.init();
    }

    public boolean setAnalysisEntity(Abundance originalAbundance, Fossil bugsData){
        if(bugsData.getSampleCODE() == null || bugsData.getSampleCODE().isEmpty()){
            originalAbundance.addError("No sample specified");
            return true;
        }
        Sample fromLastTrace = sampleTracerHelper.getFromLastTrace(bugsData.getSampleCODE());
        if(fromLastTrace == null){
            originalAbundance.addError("No sample found for code");
            return false;
        }

        SampleGroup sampleGroup = fromLastTrace.getGroup();
        Dataset dataset = datasetManager.getOrCreateFor(sampleGroup);

        if(!dataset.isErrorFree()){
            ErrorCopier.copyPotentialErrors(originalAbundance, dataset);
            return false;
        }

        AnalysisEntity ae = cache.find(fromLastTrace, dataset);
        if(ae == null && dataset.isNewItem()){
            ae = createAnalysisEntity(fromLastTrace, dataset);
        } else if(ae == null){
            List<AnalysisEntity> entitiesForSample = aeRepository.findBySampleAndDataset(fromLastTrace, dataset);
            if(entitiesForSample.isEmpty()){
                ae = createAnalysisEntity(fromLastTrace, dataset);
            } else if(entitiesForSample.size() == 1){
                ae = entitiesForSample.get(0);
            } else {
                originalAbundance.addError("More than one analysis entity found for same dataset and sample");
                return false;
            }
            cache.store(fromLastTrace, dataset, ae);
        }
        AnalysisEntity originalAbundanceAnalysisEntity = originalAbundance.getAnalysisEntity();
        originalAbundance.setAnalysisEntity(ae);
        cache.bind(ae, originalAbundance);
        return !Objects.equals(originalAbundanceAnalysisEntity, ae);
    }

    private AnalysisEntity createAnalysisEntity(Sample sample, Dataset dataset){
        AnalysisEntity ae = new AnalysisEntity();
        ae.setSample(sample);
        ae.setDataset(dataset);
        return ae;
    }

    public List<Abundance> getAbundancesFor(AnalysisEntity analysisEntity){
        return cache.getAbundances(analysisEntity);
    }
}
