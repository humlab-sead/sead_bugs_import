package se.sead.bugsimport.countsheets.converters;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.bugsmodel.CountsheetBugsTable;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.SampleGroupRepository;

@Component
public class SampleGroupBugsTraceReader extends SeadDataFromTraceHelper<Countsheet, SampleGroup>{

    public SampleGroupBugsTraceReader(SampleGroupRepository repository){
        super(CountsheetBugsTable.TABLE_NAME, false, repository);
    }

}
