package se.sead.bugsimport.countsheets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.bugsmodel.CountsheetBugsTable;
import se.sead.bugsimport.countsheets.converters.SampleGroupBugsTableRowConverter;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class SampleGroupBugsSeadMapper extends BugsSeadMapper<Countsheet, SampleGroup> {

    @Autowired
    public SampleGroupBugsSeadMapper(
            AccessReaderProvider accessReaderProvider,
            SampleGroupBugsTableRowConverter singleBugsTableRowConverterForMapper,
            BugsValueTranslationService dataTranslationService) {
        super(
                new CountsheetBugsTable(),
                singleBugsTableRowConverterForMapper
        );
    }
}
