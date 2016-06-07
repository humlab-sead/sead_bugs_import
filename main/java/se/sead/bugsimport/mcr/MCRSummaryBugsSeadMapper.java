package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryBugsTable;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.mcr.seadmodel.MCRSummary;

@Component
public class MCRSummaryBugsSeadMapper extends BugsSeadMapper<MCRSummaryData, MCRSummary> {

    @Autowired
    public MCRSummaryBugsSeadMapper(
            AccessReaderProvider accessReaderProvider,
            MCRSummaryTableRowConverter singleBugsTableRowConverterForMapper) {
        super(
                accessReaderProvider.getReader(),
                new MCRSummaryBugsTable(),
                singleBugsTableRowConverterForMapper);
    }
}
