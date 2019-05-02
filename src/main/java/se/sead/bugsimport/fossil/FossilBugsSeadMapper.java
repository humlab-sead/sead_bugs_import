package se.sead.bugsimport.fossil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.bugsmodel.FossilBugsTable;
import se.sead.bugsimport.fossil.seadmodel.Abundance;

@Component
public class FossilBugsSeadMapper extends BugsSeadMapper<Fossil, Abundance> {

    @Autowired
    public FossilBugsSeadMapper(FossilRowConverter singleBugsTableRowConverterForMapper) {
        super(new FossilBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
