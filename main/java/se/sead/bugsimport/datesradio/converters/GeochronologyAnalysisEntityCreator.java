package se.sead.bugsimport.datesradio.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.utils.ErrorCopier;

@Component
public class GeochronologyAnalysisEntityCreator {

    @Autowired
    private SampleTracerHelper sampleTracerHelper;
    @Autowired
    private GeochronologyDatasetCreator datasetCreator;

    private DatasetMaster bugsDatasetMaster;
    private DataType geochronologyDataType;

    @Autowired
    public GeochronologyAnalysisEntityCreator(
            DatasetMasterRepository masterRepository,
            DataTypeRepository dataTypeRepository
    ){
        bugsDatasetMaster = masterRepository.findBugsMasterSet();
        geochronologyDataType = dataTypeRepository.findBugsGeochronologyDataType();
    }

    public AnalysisEntity create(DatesRadio bugsData){
        assert bugsDatasetMaster != null && geochronologyDataType != null;
        return new Creator(bugsData).create();
    }

    private class Creator {
        private DatesRadio bugsData;
        private AnalysisEntity createdEntity;
        Creator(DatesRadio bugsData){
            this.bugsData = bugsData;
            createdEntity = new AnalysisEntity();
        }

        AnalysisEntity create(){
            createdEntity.setSample(getSample());
            createdEntity.setDataset(createDataset());
            return createdEntity;
        }

        private Sample getSample(){
            Sample fromLastTrace = sampleTracerHelper.getFromLastTrace(bugsData.getSampleCode());
            if(fromLastTrace == null){
                createdEntity.addError("No sample found");
            }
            return fromLastTrace;
        }

        private Dataset createDataset(){
            Dataset dataset = datasetCreator.create(bugsData);
            ErrorCopier.copyPotentialErrors(createdEntity, dataset);
            return dataset;
        }


    }
}
