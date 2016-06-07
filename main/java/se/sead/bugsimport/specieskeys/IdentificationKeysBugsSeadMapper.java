package se.sead.bugsimport.specieskeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.bugsmodel.KeysBugsTable;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;

@Component
public class IdentificationKeysBugsSeadMapper extends BugsSeadMapper<Keys, TextIdentificationKeys> {

    @Autowired
    public IdentificationKeysBugsSeadMapper(
            AccessReaderProvider accessReaderProvider,
            KeysTableRowConverter singleBugsTableRowConverterForMapper) {
        super(
                accessReaderProvider.getReader(),
                new KeysBugsTable(),
                singleBugsTableRowConverterForMapper);
    }
}
