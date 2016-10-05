package se.sead.bugsimport.countsheets.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.bugsmodel.CountsheetBugsTable;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.SampleGroupRepository;

import java.util.List;

@Component
public class SampleGroupBugsTraceReader extends SeadDataFromTraceHelper<Countsheet, SampleGroup>{

    public SampleGroupBugsTraceReader(SampleGroupRepository repository){
        super(CountsheetBugsTable.TABLE_NAME, false, repository);
    }

}
