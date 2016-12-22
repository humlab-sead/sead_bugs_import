package se.sead.bugsimport.sitereferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessDatabaseProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.sitereferences.bugsmodel.SiteRefBugsTable;
import se.sead.bugsimport.sitereferences.converters.SiteReferenceBugsTableRowConverter;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class SiteReferenceBugsSeadMapper extends BugsSeadMapper<BugsSiteRef, SiteReference> {

    @Autowired
    public SiteReferenceBugsSeadMapper(
            AccessDatabaseProvider accessDatabaseProvider,
            SiteReferenceBugsTableRowConverter singleBugsTableRowConverterForMapper,
            BugsValueTranslationService dataTranslationService) {
        super(
                new SiteRefBugsTable(),
                singleBugsTableRowConverterForMapper
        );
    }
}
