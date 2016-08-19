package se.sead.bugsimport.sitelocations.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocationBugsTable;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.SiteLocationRepository;

@Component
public class SiteLocationTraceHelper extends SeadDataFromTraceHelper<BugsSiteLocation, SiteLocation>{

    private static final String TABLE_NAME = new BugsSiteLocationBugsTable().getTableName();

    @Autowired
    public SiteLocationTraceHelper(SiteLocationRepository siteLocationRepository) {
        super(TABLE_NAME, true, siteLocationRepository);
    }
}
