package se.sead.bugsimport.fossil.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.sead.data.Abundance;
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

    private SampleTracerHelper sampleTracerHelper;
    private AnalysisEntityRepository aeRepository;
    private DatasetManagerWithCreation datasetManagerWithCreation;

    private boolean updateDatasetConfiguration;

    private AnalysisEntityCache cache = new AnalysisEntityCache();

    @Autowired
    public AnalysisEntityManager(
            SampleTracerHelper sampleTracerHelper,
            AnalysisEntityRepository aeRepository,
            DatasetManagerWithCreation datasetManagerWithCreation,
            @Value("${allow.dataset.updates:true}")
            Boolean updateDatasetConfig
    ){
        this.sampleTracerHelper = sampleTracerHelper;
        this.aeRepository = aeRepository;
        this.datasetManagerWithCreation = datasetManagerWithCreation;
        this.updateDatasetConfiguration = updateDatasetConfig;
    }

    public void initCache(){
        cache.init();
    }

    public boolean setAnalysisEntity(Abundance originalAbundance, Fossil bugsData, boolean abundanceDataChanged){
        if(bugsData.getSampleCODE() == null || bugsData.getSampleCODE().isEmpty()){
            originalAbundance.addError("No sample specified");
            return true;
        }
        Sample sampleFromLastTrace = sampleTracerHelper.getFromLastTrace(bugsData.getSampleCODE());
        if(sampleFromLastTrace == null){
            originalAbundance.addError("No sample found for code");
            return false;
        }

        SampleGroup sampleGroup = sampleFromLastTrace.getGroup();
        Dataset dataset = datasetManagerWithCreation.getOrCreateFor(sampleGroup);

        if(!dataset.isErrorFree()){
            ErrorCopier.copyPotentialErrors(originalAbundance, dataset);
            return false;
        }

        if(shouldCreateNewDatasetWithOriginalAsLink(dataset) && abundanceDataChanged){
            dataset = datasetManagerWithCreation.updateDataset(dataset);
        }

        AnalysisEntity ae = cache.find(sampleFromLastTrace, dataset);

        if(ae == null && dataset.isNewItem()){
            // System.out.format("Analysis entity for %s NOT FOUND in cache (new ds). sample %s dataset %s", bugsData.getFossilBugsCODE(), sampleFromLastTrace.getName(), dataset.getName());
            ae = createAnalysisEntity(sampleFromLastTrace, dataset);
            cache.store(sampleFromLastTrace, dataset, ae);
        } else if(ae == null){
            // System.out.format("Analysis entity for %s NOT FOUND in cache. Searching in SEAD. Sample %s dataset %s", bugsData.getFossilBugsCODE(), sampleFromLastTrace.getName(), dataset.getName());
            List<AnalysisEntity> entitiesForSample = aeRepository.findBySampleAndDataset(sampleFromLastTrace, dataset);
            if(entitiesForSample.isEmpty()){
                ae = createAnalysisEntity(sampleFromLastTrace, dataset);
            } else if(entitiesForSample.size() == 1){
                ae = entitiesForSample.get(0);
            } else {
                originalAbundance.addError("More than one analysis entity found for same dataset and sample");
                return false;
            }
            cache.store(sampleFromLastTrace, dataset, ae);
        // } else {
        //    System.out.format("Analysis entity for %s FOUND in cache. sample %s dataset %s", bugsData.getFossilBugsCODE(), sampleFromLastTrace.getName(), dataset.getName());
        }
        AnalysisEntity originalAbundanceAnalysisEntity = originalAbundance.getAnalysisEntity();
        originalAbundance.setAnalysisEntity(ae);
        ae.getAbundances().add(originalAbundance);
        return !Objects.equals(originalAbundanceAnalysisEntity, ae);
    }

    private boolean shouldCreateNewDatasetWithOriginalAsLink(Dataset dataset){
        return !updateDatasetConfiguration &&
                !dataset.isNewItem();
    }

    private AnalysisEntity createAnalysisEntity(Sample sample, Dataset dataset){
        AnalysisEntity ae = new AnalysisEntity();
        ae.setSample(sample);
        ae.setDataset(dataset);
        return ae;
    }

}
