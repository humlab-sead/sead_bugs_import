package se.sead.bugsimport.datasetcontacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.bugsmodel.CountsheetBugsTable;
import se.sead.sead.data.DatasetContact;

@Component
public class DatasetContactBugsSeadMapper extends BugsSeadMapper<Countsheet, DatasetContact> {

    @Autowired
    public DatasetContactBugsSeadMapper(
            DatasetContactsRowConverter singleBugsTableRowConverterForMapper
    ) {
        super(new CountsheetBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
