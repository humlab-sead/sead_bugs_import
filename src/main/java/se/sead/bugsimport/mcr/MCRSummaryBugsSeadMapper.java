package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessDatabaseProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryBugsTable;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class MCRSummaryBugsSeadMapper extends BugsSeadMapper<MCRSummaryData, MCRSummary> {

    @Autowired
    public MCRSummaryBugsSeadMapper(
            AccessDatabaseProvider accessDatabaseProvider,
            MCRSummaryTableRowConverter singleBugsTableRowConverterForMapper,
            BugsValueTranslationService dataTranslationService) {
        super(
                new MCRSummaryBugsTable(),
                singleBugsTableRowConverterForMapper
        );
    }
}
