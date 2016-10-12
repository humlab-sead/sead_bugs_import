package se.sead.bugsimport.datesradio.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;

@Component
public class GeochronologyDatasetCreator {

    @Autowired
    private GeochronologyMethodManager methodManager;

    private DatasetMaster bugsDatasetMaster;
    private DataType geochronologyDataType;

    @Autowired
    public GeochronologyDatasetCreator(
            DatasetMasterRepository masterRepository,
            DataTypeRepository dataTypeRepository
    ){
        bugsDatasetMaster = masterRepository.findBugsMasterSet();
        geochronologyDataType = dataTypeRepository.findBugsGeochronologyDataType();
    }

    public Dataset create(DatesRadio bugsData){
        assert bugsDatasetMaster != null && geochronologyDataType != null;
        Dataset newDataset = new Dataset();
        newDataset.setName(bugsData.getDateCode());
        newDataset.setDataType(geochronologyDataType);
        newDataset.setMasterDataset(bugsDatasetMaster);

        setMethod(newDataset, bugsData.getDatingMethod());
        return newDataset;
    }

    private void setMethod(Dataset dataset, String datingMethod) {
        Method seadDatingMethod = methodManager.get(datingMethod);
        if(seadDatingMethod == null){
            dataset.addError("No method found");
        }
        dataset.setMethod(seadDatingMethod);
    }
}
