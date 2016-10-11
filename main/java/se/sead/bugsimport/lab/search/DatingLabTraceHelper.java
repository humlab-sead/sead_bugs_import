package se.sead.bugsimport.lab.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.bugsmodel.LabBugsTable;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.DatingLabRepository;

@Component
public class DatingLabTraceHelper extends SeadDataFromTraceHelper<BugsLab, DatingLab> {

    @Autowired
    public DatingLabTraceHelper(DatingLabRepository repository){
        super(LabBugsTable.TABLE_NAME, false, repository);
    }
}
