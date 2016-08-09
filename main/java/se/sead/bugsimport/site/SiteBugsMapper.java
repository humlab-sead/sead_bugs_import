package se.sead.bugsimport.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.bugsmodel.BugsSiteBugsTable;
import se.sead.bugsimport.site.conversion.BugsSiteTableConverter;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class SiteBugsMapper extends BugsSeadMapper<BugsSite, SeadSite>{

    @Autowired
    public SiteBugsMapper(
            AccessReaderProvider accessReaderProvider,
            BugsSiteTableConverter converter,
            BugsValueTranslationService dataTranslationService) {
        super(
                accessReaderProvider.getReader(),
                new BugsSiteBugsTable(),
                converter,
                dataTranslationService);
    }
}
