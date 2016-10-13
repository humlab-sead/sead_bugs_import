package se.sead.bugsimport.periods.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.bugsmodel.PeriodBugsTable;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.RelativeAgeRepository;

@Component
public class PeriodTraceHelper extends SeadDataFromTraceHelper<Period, RelativeAge> {

    @Autowired
    public PeriodTraceHelper(RelativeAgeRepository repository){
        super(PeriodBugsTable.TABLE_NAME, "tbl_relative_ages", false, repository);
    }
}
