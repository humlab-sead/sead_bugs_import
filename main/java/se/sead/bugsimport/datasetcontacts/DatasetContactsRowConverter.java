package se.sead.bugsimport.datasetcontacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.datasetcontacts.updater.DatasetContactUpdater;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetContact;

import java.util.Arrays;
import java.util.List;

@Component
public class DatasetContactsRowConverter implements BugsTableRowConverter<Countsheet, DatasetContact> {

    @Autowired
    private DatasetManagerNoCreation datasetManager;
    @Autowired
    private DatasetContactUpdater updater;

    @Override
    public List<DatasetContact> convertListForDataRow(Countsheet bugsData) {
        Dataset dataset = datasetManager.find(bugsData.getCode());
        if(!dataset.isErrorFree()){
            return createWithError("No dataset for count sheet code found");
        } else {
            updater.update(dataset, bugsData.getSiteCode());
            return dataset.getContacts();
        }
    }

    private List<DatasetContact> createWithError(String error){
        DatasetContact datasetContact = new DatasetContact();
        datasetContact.addError(error);
        return Arrays.asList(datasetContact);
    }
}
