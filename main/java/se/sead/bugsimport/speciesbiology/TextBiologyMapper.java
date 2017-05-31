package se.sead.bugsimport.speciesbiology;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.SetMappingResult;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.bugsmodel.BiologyBugsTable;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;

@Component
public class TextBiologyMapper extends BugsSeadMapper<Biology, TextBiology> {

    @Autowired
    public TextBiologyMapper(BiologyToTextBiologyRowConverter rowConverter) {
        super(new BiologyBugsTable(), rowConverter);
    }

    @Override
    protected MappingResult<Biology, TextBiology> initMapperResultContainer() {
        return new SetMappingResult<>();
    }
}
