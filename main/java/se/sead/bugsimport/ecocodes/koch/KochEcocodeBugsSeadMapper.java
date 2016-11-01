package se.sead.bugsimport.ecocodes.koch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKochBugsTable;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;

@Component
public class KochEcocodeBugsSeadMapper extends BugsSeadMapper<EcoKoch, Ecocode>{

    @Autowired
    public KochEcocodeBugsSeadMapper(KochEcocodesRowConverter singleBugsTableRowConverterForMapper) {
        super(new EcoKochBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
