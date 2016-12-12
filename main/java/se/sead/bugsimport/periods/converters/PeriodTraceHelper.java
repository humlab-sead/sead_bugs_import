package se.sead.bugsimport.periods.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.bugsmodel.PeriodBugsTable;
import se.sead.bugsimport.periods.bugsmodel.PeriodFromTraceBuilder;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.repositories.RelativeAgeRepository;

import java.util.List;

@Component
public class PeriodTraceHelper extends SeadDataFromTraceHelper<Period, RelativeAge> {

    public static final Period NO_PERIOD_FOUND;

    static {
        NO_PERIOD_FOUND = new Period();
        NO_PERIOD_FOUND.setPeriodCode("-1");
        NO_PERIOD_FOUND.setName("Error");
        NO_PERIOD_FOUND.setDesc("");
        NO_PERIOD_FOUND.setYearsType("");
    }

    @Autowired
    public PeriodTraceHelper(RelativeAgeRepository repository){
        super(PeriodBugsTable.TABLE_NAME, "tbl_relative_ages", false, repository);
    }

    public Period getPeriodFromTrace(RelativeAge seadValue){
        List<BugsTrace> agesById = traceRepository.findBySeadTableAndSeadIdOrderByChangeDate(getSeadTableName(), seadValue.getId());
        if(agesById.isEmpty()){
            return NO_PERIOD_FOUND;
        } else {
            BugsTrace updateOrInsertTrace = getFirstUpdateOrInsertType(agesById);
            if(updateOrInsertTrace == null){
                return NO_PERIOD_FOUND;
            }
            PeriodFromTraceBuilder periodBuilder = new PeriodFromTraceBuilder(updateOrInsertTrace);
            return periodBuilder.createPeriodFromTrace();
        }
    }

    private BugsTrace getFirstUpdateOrInsertType(List<BugsTrace> agesById){
        for (BugsTrace trace :
                agesById) {
            if (trace.getType() == BugsTraceType.INSERT ||
                trace.getType() == BugsTraceType.UPDATE ||
                trace.getType() == null) {
                return trace;
            }
        }
        return null;
    }

}
