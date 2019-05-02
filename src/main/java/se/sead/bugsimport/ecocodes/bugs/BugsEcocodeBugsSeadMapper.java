package se.sead.bugsimport.ecocodes.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugsBugsTable;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;

@Component
public class BugsEcocodeBugsSeadMapper extends BugsSeadMapper<EcoBugs, Ecocode> {

    @Autowired
    public BugsEcocodeBugsSeadMapper(BugsEcocodeRowConverter singleBugsTableRowConverterForMapper) {
        super(new EcoBugsBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
