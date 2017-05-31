package se.sead.bugsimport.specieskeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessDatabaseProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.SetMappingResult;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.bugsmodel.KeysBugsTable;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class IdentificationKeysBugsSeadMapper extends BugsSeadMapper<Keys, TextIdentificationKeys> {

    @Autowired
    public IdentificationKeysBugsSeadMapper(
            AccessDatabaseProvider accessDatabaseProvider,
            KeysTableRowConverter singleBugsTableRowConverterForMapper,
            BugsValueTranslationService dataTranslationService) {
        super(
                new KeysBugsTable(),
                singleBugsTableRowConverterForMapper
        );
    }

    @Override
    protected MappingResult<Keys, TextIdentificationKeys> initMapperResultContainer() {
        return new SetMappingResult<>();
    }
}
