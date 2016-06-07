package se.sead.bugsimport.speciesbiology;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.bugsmodel.BiologyBugsTable;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;

/**
 * Created by erer0001 on 2016-05-18.
 */
@Component
public class TextBiologyMapper extends BugsSeadMapper<Biology, TextBiology> {

    @Autowired
    public TextBiologyMapper(
            AccessReaderProvider accessReaderProvider,
            BiologyToTextBiologyRowConverter rowConverter) {
        super(accessReaderProvider.getReader(), new BiologyBugsTable(), rowConverter);
    }
}
