package se.sead.bugsimport.fossil.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.bugsmodel.FossilBugsTable;
import se.sead.sead.data.Abundance;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.AbundanceRepository;

@Component
public class FossilTraceHelper extends SeadDataFromTraceHelper<Fossil, Abundance> {

    @Autowired
    public FossilTraceHelper(AbundanceRepository repository){
        super(FossilBugsTable.TABLE_NAME, "tbl_abundances", false, repository);
    }
}
