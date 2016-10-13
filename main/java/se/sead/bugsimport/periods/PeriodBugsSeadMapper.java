package se.sead.bugsimport.periods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.bugsmodel.PeriodBugsTable;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;

@Component
public class PeriodBugsSeadMapper extends BugsSeadMapper<Period, RelativeAge> {

    @Autowired
    public PeriodBugsSeadMapper(PeriodRowConverter singleBugsTableRowConverterForMapper) {
        super(new PeriodBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
