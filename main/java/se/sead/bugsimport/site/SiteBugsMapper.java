package se.sead.bugsimport.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessDatabaseProvider;
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
            AccessDatabaseProvider accessDatabaseProvider,
            BugsSiteTableConverter converter,
            BugsValueTranslationService dataTranslationService) {
        super(
                new BugsSiteBugsTable(),
                converter
        );
    }
}
